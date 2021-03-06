docker run -d --name cassandradb --network mynetwork cassandra
docker run -it --link cassandradb:cassandra --rm cassandra cqlsh cassandra

CREATE KEYSPACE cinemadb  WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3};

use cinemadb;

CREATE TABLE cinemadb.cinema (
    id int PRIMARY KEY,
    moviename text,
    seatid text,
    status text
) ;

insert into cinema(id,moviename,seatid,status) values(9,'justice league','b4','nb');
insert into cinema(id,moviename,seatid,status) values(5,'justice league','a5','nb');
insert into cinema(id,moviename,seatid,status) values(12,'The Lego','a2','nb');
insert into cinema(id,moviename,seatid,status) values(8,'justice league','b3','nb');
insert into cinema(id,moviename,seatid,status) values(3,'justice league','a3','nb');
insert into cinema(id,moviename,seatid,status) values(2,'justice league','a2','nb');
insert into cinema(id,moviename,seatid,status) values(7,'justice league','b2','nb');
insert into cinema(id,moviename,seatid,status) values(4,'justice league','a4','nb');
insert into cinema(id,moviename,seatid,status) values(18,'The Lego','b3','nb');
insert into cinema(id,moviename,seatid,status) values(20,'The Lego','b5','nb');
insert into cinema(id,moviename,seatid,status) values(11,'The Lego','a1','nb');
insert into cinema(id,moviename,seatid,status) values(1,'justice league','a1','nb');
insert into cinema(id,moviename,seatid,status) values(19,'The Lego','b4','nb');
insert into cinema(id,moviename,seatid,status) values(14,'The Lego','a4','nb');
insert into cinema(id,moviename,seatid,status) values(17,'The Lego','b2','nb');
insert into cinema(id,moviename,seatid,status) values(15,'The Lego','a5','nb');
insert into cinema(id,moviename,seatid,status) values(6,'justice league','b1','nb');
insert into cinema(id,moviename,seatid,status) values(16,'The Lego','b1','nb');
insert into cinema(id,moviename,seatid,status) values(10,'justice league','b5','nb');
insert into cinema(id,moviename,seatid,status) values(13,'The Lego','a3','nb');
