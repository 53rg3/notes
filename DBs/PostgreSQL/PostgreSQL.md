# PostgreSQL



## Todo

- Connection pooling? https://www.digitalocean.com/blog/some-postgres-best-practices/





## Install

- Install from repository. Ubuntu has only old versions. See [here](https://www.postgresql.org/download/linux/ubuntu/).
- You need to `CREATE USER` and `ALTER USER your_user PASSWORD` in `psql` (see commands).





## Nice2Knows

- [Cheat Sheet](http://www.jancarloviray.com/blog/postgres-quick-start-and-best-practices/)

- (CREATE USER is equivalent to CREATE ROLE except that CREATE USER includes LOGIN by default, while CREATE ROLE does not). So just create a USER.

- Only user with role CREATEDB can create DBs. You can simply use USER `postgres` for that. A normal USER can then create tables.

- Use SEQUENCE for primary keys, see [here](https://thorben-janssen.com/hibernate-postgresql-5-things-need-know/#1_Mappings_Primary_Keys).

- If you want to store JSON, you should probably check JSONB. 

  



## Commands

- Start Postgres, see [here](https://dba.stackexchange.com/a/156722)

  ```bash
  sudo service postgresql start
  ```

- CLI (where you can type SQL command in terminal). So you're running `psql` as user "postgres".

  ```
  sudo -u postgres psql template1
  ```

- Create user "cc". Doesn't allow to create DBs. Probably use `psql` and roles from [here](https://www.postgresql.org/docs/13/role-attributes.html).

  ```bash
  sudo -u postgres createuser cc
  ```

- Change password for user, see [here](https://stackoverflow.com/a/7696398/4179212)

  ```
  ALTER USER postgres PASSWORD 'newPassword';
  ```

- Show roles

  ```sql
  SELECT rolname FROM pg_roles;
  ```

- PSQL commands

  - Helper functions (just type into psql) 

    ```
  \conninfo				# info about current connection with user etc.
    \list					# list all databases
    \connect db_name		# connects you to DB with name db_name			
    ```
    
    
    
  - Check [System Information Functions and Operators](https://www.postgresql.org/docs/current/functions-info.html). You can show these the variables (not functions, those with brackets) via SQL queries:
  
    ```
  SELECT current_user;
    ```
    
    

## How To

### Create tables (manually)

- In "Query Tool" (see left menu, below "Object" option) you can run SQL queries. Table can be found under 

  ```
  Databases.your_db.Schemas.public.your_table
  ```

  Right-click on "Tables" option to refresh, if not shown.

### Configure as data source in IntelliJ

- Click through the error messages and download Postgres Driver, or see [here](https://www.jetbrains.com/help/idea/connecting-to-a-database.html#connect-to-postgresql-database)
- To add a user click on the + in the left upper corner. Set user, pw and YOUR database (not postgres).



## Encountered Problems

### Error messages

- createdb: error: could not connect to database template1: FATAL:  role "cc" does not exist
  See [here](https://stackoverflow.com/questions/11919391/postgresql-error-fatal-role-username-does-not-exist/18708583#18708583).
- Try to DROP DATABASE .... but: ERROR:  database "mydb" is being accessed by other users
  See [here](https://stackoverflow.com/a/53471347/4179212).

