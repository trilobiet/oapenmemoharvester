# OAPEN MEMO XOAI Harvester

## What is it?

A harvester service that populates the OAPEN MEMO database with data from the OAPEN DSpace XOAI provider.

## What does it do?
 
1. Read last harvest date;
2. harvest XOAI since last harvest date;
3. parse records and save to database;
4. fetch corresponding export chunks and save to database;
5. Update last harvest date.

Configuration settings are read from `application.properties`

Harvesting may use a negative amount of offset days from the current date, to ensure only redacted and matured data is harvested.  
Use `app.harvest.daysBack = 7` to set an offset period of 7 days.

## How to run it?

Run as an executable jar: 

	./harvester-x.y.z.jar

Add an integer argument to override the `app.harvest.daysBack` value as set in the properties file:

	./harvester-x.y.z.jar 5

Typically you want to run this as a cronjob (for the corresponding user) once a day:

    0 1 * * * ~/harvester-x.y.z.jar >/dev/null 2>&1 


## Anything else?

- Application status (last harvest date etc.) is saved to a directory `[user.home]/oapenmemo`;
- Downloaded export files are saved to a directory `[user.home]/oapenmemo/downloads`. These files are only downloaded once, after the initial harvest. After the initial harvest, export data chunks are requested separately from the DSpace API;
- logs are saved to a directory `[user.home]/oapenmemo/logs`