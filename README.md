# GoMovie
edit configuration in application.conf in common/src/main/resources

senderId="senderId" mailPassword="password" dbUrl="dburl" dbUserName="username" dbpassword="password"

Database creation and insert commands is in sqlCommands.txt

Run apiMain,selectorMain and notifierMain 

Hit url like 
localhost:9001/bookSeat?mailId=abc@gmail.com&movieName=The Lego&count=2

for creating jars
sbt assembly
