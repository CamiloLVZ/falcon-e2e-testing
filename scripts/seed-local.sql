BEGIN;
-- ----------------------------------------------------------------------------
-- 1. Full reset
-- ----------------------------------------------------------------------------
TRUNCATE TABLE
    public.boarding_pass,
    public.passenger_reservation,
    public.passenger,
    public.payment,
    public.reservation,
    public.password_reset_token,
    public.flight,
    public.flight_generation,
    public.user_roles,
    public.users,
    public.route_schedule,
    public.route_day,
    public.route,
    public.airport,
    public.airplane_type,
    public.country,
    public.roles
    RESTART IDENTITY CASCADE;

-- ----------------------------------------------------------------------------
-- 2. Reference catalog
-- ----------------------------------------------------------------------------
INSERT INTO public.country (name, iso_code) VALUES
                                                ('Colombia', 'CO'),
                                                ('United States', 'US');

INSERT INTO public.airport (name, city, iata_code, id_country, timezone)
SELECT v.name, v.city, v.iata_code, c.id, v.timezone
FROM (VALUES
          ('Aeropuerto Internacional El Dorado',            'Bogotá',        'BOG', 'CO', 'America/Bogota'),
          ('Aeropuerto Internacional José María Córdova',   'Medellín',      'MDE', 'CO', 'America/Bogota'),
          ('Aeropuerto Internacional Alfonso Bonilla Aragón','Cali',         'CLO', 'CO', 'America/Bogota'),
          ('Aeropuerto Internacional Rafael Núñez',         'Cartagena',     'CTG', 'CO', 'America/Bogota'),
          ('Aeropuerto Internacional Ernesto Cortissoz',    'Barranquilla',  'BAQ', 'CO', 'America/Bogota'),
          ('Aeropuerto Internacional Simón Bolívar',        'Santa Marta',   'SMR', 'CO', 'America/Bogota'),
          ('Aeropuerto Internacional Gustavo Rojas Pinilla','San Andrés',    'ADZ', 'CO', 'America/Bogota'),
          ('Aeropuerto Internacional Vanguardia',           'Villavicencio', 'VVC', 'CO', 'America/Bogota'),
          ('Miami International',                           'Miami',         'MIA', 'US', 'America/New_York')
     ) AS v(name, city, iata_code, country_iso, timezone)
         JOIN public.country c ON c.iso_code = v.country_iso;

INSERT INTO public.airplane_type (producer, model, economy_seats, first_class_seats, status, seat_columns) VALUES
                                                                                                               ('AIRBUS', 'A320',   120, 30, 'ACTIVE', 'ABCDEF'),
                                                                                                               ('BOEING', '787-8',  220, 50, 'ACTIVE', 'ABCDEFGHI');

-- Routes: same pattern as production, same base prices
INSERT INTO public.route (id_airport_origin, id_airport_destination, flight_number, id_default_airplane_type, duration_minutes, status, base_price_economy, base_price_first_class)
SELECT o.id, d.id, v.flight_number, at.id, v.duration_minutes, 'ACTIVE', v.price_economy, v.price_first
FROM (VALUES
          ('BOG','MDE','FAL121',  38,150000,400000),
          ('BOG','ADZ','FAL5632', 85,200000,450000),
          ('BOG','VVC','FAL5432', 34,100000,220000),
          ('BOG','BAQ','FAL2618', 48,160000,260000),
          ('BOG','SMR','FAL2539', 67,190000,320000),
          ('BOG','MIA','FAL024', 235,780000,1800000),
          ('BOG','CTG','FAL7842', 72,195000,300000),
          ('ADZ','BOG','FAL6841', 85,200000,450000),
          ('VVC','BOG','FAL6818', 38,100000,220000),
          ('BOG','CLO','FAL253',  49,140000,230000),
          ('BAQ','BOG','FAL6816', 48,160000,260000),
          ('SMR','BOG','FAL2384', 67,190000,320000),
          ('CLO','BOG','FAL3792', 49,140000,230000),
          ('MDE','BOG','FAL3097', 38,150000,400000),
          ('CTG','BOG','FAL5312', 72,195000,300000),
          ('MIA','BOG','FAL025', 245,780000,1800000)
     ) AS v(origin_iata, dest_iata, flight_number, duration_minutes, price_economy, price_first)
         JOIN public.airport o ON o.iata_code = v.origin_iata
         JOIN public.airport d ON d.iata_code = v.dest_iata
         JOIN public.airplane_type at ON at.producer = 'AIRBUS' AND at.model = 'A320';

-- All routes operate every day
INSERT INTO public.route_day (id_route, day_of_week)
SELECT r.id, d.day_of_week
FROM public.route r
         CROSS JOIN (VALUES ('MONDAY'),('TUESDAY'),('WEDNESDAY'),('THURSDAY'),('FRIDAY'),('SATURDAY'),('SUNDAY')) AS d(day_of_week);

-- 2 departure schedules per route
INSERT INTO public.route_schedule (id_route, departure_local_time)
SELECT r.id, t.departure_time
FROM public.route r
         CROSS JOIN (VALUES ('06:00:00'::time), ('18:30:00'::time)) AS t(departure_time);

-- ----------------------------------------------------------------------------
-- 3. Test roles and users
-- ----------------------------------------------------------------------------

INSERT INTO public.roles (name) VALUES ('ADMIN'), ('CLIENT');

-- password: "Test1234!"
-- password: "Admin1234!"
INSERT INTO public.users (email, password, disabled) VALUES
                                                         ('e2e.client@falcon.test', '$2b$10$sjfjSyQnlHvrWhxuFPjysOxbJCqLuOt0O/b4BMjt4IYctzRUhW8Na', false),
                                                         ('e2e.admin@falcon.test',  '$2b$10$vZFiDCbpcRW/b.UzGFz2Iepb5MUpomt8YhMAGbyeoAtTT9NoNADhq', false);

INSERT INTO public.user_roles (id_user, id_role)
SELECT u.id, r.id FROM public.users u, public.roles r
WHERE (u.email = 'e2e.client@falcon.test' AND r.name = 'CLIENT')
   OR (u.email = 'e2e.admin@falcon.test'  AND r.name = 'ADMIN');

-- ----------------------------------------------------------------------------
-- 4. Flights — generated relative to TODAY
-- ----------------------------------------------------------------------------
INSERT INTO public.flight (id_route, departure_datetime, id_airplane_type, status, base_price_economy, base_price_first_class)
SELECT
    r.id,
    (CURRENT_DATE + gs.day_offset + rs.departure_local_time) AT TIME ZONE 'UTC',
    r.id_default_airplane_type,
    'SCHEDULED',
    r.base_price_economy,
    r.base_price_first_class
FROM public.route r
         JOIN public.route_day rd
              ON rd.id_route = r.id
         JOIN public.route_schedule rs
              ON rs.id_route = r.id
         CROSS JOIN generate_series(1, 21) AS gs(day_offset)
WHERE r.status = 'ACTIVE'
  AND rd.day_of_week = TRIM(TO_CHAR(CURRENT_DATE + gs.day_offset, 'FMDAY'));

COMMIT;

-- ----------------------------------------------------------------------------
-- Quick verification
-- ----------------------------------------------------------------------------
SELECT 'airports' AS tabla, COUNT(*) FROM public.airport
UNION ALL SELECT 'routes', COUNT(*) FROM public.route
UNION ALL SELECT 'flights', COUNT(*) FROM public.flight
UNION ALL SELECT 'flights_next_7_days', COUNT(*) FROM public.flight WHERE departure_datetime < now() + interval '7 days'
UNION ALL SELECT 'users', COUNT(*) FROM public.users;