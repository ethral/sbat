CREATE TABLE sbat.booking (

    booking_skey BIGSERIAL,
    booking_token character varying NOT NULL PRIMARY KEY,
    booking_type character varying NOT NULL,
    full_name character varying NOT NULL,
    email character varying NOT NULL,
    phone_no numeric,
    booking_slot timestamp with time zone
);



CREATE INDEX idx_booking_slot ON sbat.booking(booking_slot);




  INSERT INTO sbat.booking (booking_token,booking_type,full_name,email,phone_no,booking_slot)
  VALUES ('FE20092808','Archanam','Sai Prasad','sai.jesus@gmail.com',8676667877,'2020-09-30 13:00:00');

  INSERT INTO sbat.booking (booking_token,booking_type,full_name,email,phone_no,booking_slot)
  VALUES ('XE20092808','Archanam','Boby Prasad','bob.jesus@gmail.com',6676667877,'2020-09-28 13:00:00');



   INSERT INTO sbat.booking (booking_token,booking_type,full_name,email,phone_no,booking_slot)
  VALUES ('AE20092808','Archanam','Sai Johnny','johnny.jesus@gmail.com',8676667877,'2020-09-28 11:30:00');


   INSERT INTO sbat.booking (booking_token,booking_type,full_name,email,phone_no,booking_slot)
  VALUES ('QE20092808','Archanam','Sai Olive','olive.jesus@gmail.com',8676667877,'2020-09-28 11:30:00');


   INSERT INTO sbat.booking (booking_token,booking_type,full_name,email,phone_no,booking_slot)
  VALUES ('RE20092808','Archanam','Sai Babu','babu.jesus@gmail.com',8676667877,'2020-09-28 11:30:00');

   INSERT INTO sbat.booking (booking_token,booking_type,full_name,email,phone_no,booking_slot)
  VALUES ('OE20092808','Archanam','Sai James','james.jesus@gmail.com',8676667877,'2020-09-28 11:30:00');

  INSERT INTO sbat.booking (booking_token,booking_type,full_name,email,phone_no,booking_slot)
  VALUES ('PE20092808','Archanam','Rocky James','rocky.jesus@gmail.com',8676667877,'2020-09-28 11:30:00');