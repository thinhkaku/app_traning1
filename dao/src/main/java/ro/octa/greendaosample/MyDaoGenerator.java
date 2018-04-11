package ro.octa.greendaosample;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * @author Octa
 */
public class MyDaoGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");
    private static final String OUT_DIR = PROJECT_DIR + "/app/src/main/java";

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "net.mcc3si.databasesv5");

//        addTablesLogbook(schema);
//        addTablesAirfield(schema);
        //addTablesPilot(schema);
        addTablesPilotLog(schema);
        new DaoGenerator().generateAll(schema, OUT_DIR);
    }

//    /**
//     * Create tables and the relationships between them
//     */
//    private static void addTables(Schema schema) {
//        /* entities */
//        Entity user = addUser(schema);
//        Entity userDetails = addUserDetails(schema);
//        Entity phoneNumber = addPhoneNumber(schema);
//
//        /* properties */
//        Property userIdForUserDetails = userDetails.addLongProperty("userId").notNull().getProperty();
//        Property userDetailsIdForUser = user.addLongProperty("detailsId").notNull().getProperty();
//        Property userDetailsIdForPhoneNumber = phoneNumber.addLongProperty("detailsId").notNull().getProperty();
//
//        /* relationships between entities */
//        userDetails.addToOne(user, userIdForUserDetails, "user");    // one-to-one (user.getDetails)
//        user.addToOne(userDetails, userDetailsIdForUser, "details"); // one-to-one (user.getUser)
//
//        ToMany userDetailsToPhoneNumbers = userDetails.addToMany(phoneNumber, userDetailsIdForPhoneNumber);
//        userDetailsToPhoneNumbers.setName("phoneNumbers"); // one-to-many (userDetails.getListOfPhoneNumbers)
//    }

    private static void addTablesLogbook(Schema schema) {
        /*entities*/
        //addCountry(schema);
        // addDelay(schema);
        //addFlight(schema);
        // addTails(schema);
        //addCurrencies(schema);

        //country has many tail 1-n
//        Property countryId = tail.addStringProperty("countryCode").notNull().getProperty();
//        ToMany countryToTail = country.addToMany(tail, countryId);
//        countryToTail.setName("tailList");
    }


    private static void addTablesAirfield(Schema schema) {
        /*entities*/
        // addAirfield(schema);
        //addCountryGeo(schema);
        //addTimeZone(schema);
    }

    private static void addTablesPilot(Schema schema) {
        /*entities*/
//        addAircraft(schema);
//        addAirfieldPilot(schema);
//        addCountry(schema);
//        addDBVersion(schema);
//        addFlightPilot(schema);
//        addLaunches(schema);
//        addPilot(schema);
//        addSetting(schema);
//        addTotal(schema);
        //addTypeOfApp(schema);
//        addWeather(schema);
    }

   /* private static Entity addSignature(Schema schema) {
        Entity signature = schema.addEntity("Signature");
        signature.addIdProperty().primaryKey().autoincrement();
        signature.addStringProperty("Name").notNull();
        return signature;
    }

    *//**
     * Create user's Properties
     *
     * @return DBUser entity
     *//*
    private static Entity addUser(Schema schema) {
        Entity user = schema.addEntity("DBUser");
        user.addIdProperty().primaryKey().autoincrement();
        user.addStringProperty("email").notNull().unique();
        user.addStringProperty("password").notNull();
        return user;
    }

    *//**
     * Create currencies's Properties
     *
     * @return Currencies entity
     *//*
    private static Entity addCurrency(Schema schema) {
        Entity currency = schema.addEntity("Currencies");
        currency.addIdProperty().primaryKey().autoincrement();
        currency.addStringProperty("ColorCode").notNull();
        currency.addStringProperty("ExpDate").notNull();
        currency.addStringProperty("Text").notNull();
        currency.addStringProperty("WarnPeriod").notNull();
        return currency;
    }

    *//**
     * Create country's Properties
     *
     * @return country entity
     *//*
    private static Entity addCountry(Schema schema) {
        Entity country = schema.addEntity("Country");
        country.addStringProperty("Code").primaryKey();
        country.addStringProperty("Country").notNull();
        country.addStringProperty("ICAO").notNull();
        country.addStringProperty("ISO").notNull();
        return country;
    }

    *//**
     * Create delay's Properties
     *
     * @return delay entity
     *//*
    private static Entity addDelay(Schema schema) {
        Entity delay = schema.addEntity("Delay");
        delay.addStringProperty("Numeric").primaryKey();
        delay.addStringProperty("Code").notNull().unique();
        delay.addStringProperty("Name").notNull();
        delay.addStringProperty("Description");
        return delay;
    }

    *//**
     * Create flight's Properties
     *
     * @return flight entity
     *//*
    private static Entity addFlight(Schema schema) {
        Entity flight = schema.addEntity("lbflight");
        flight.addLongProperty("ArrCode");
        flight.addLongProperty("ArrTime");
        flight.addLongProperty("CostPerDiem");
        flight.addLongProperty("CostPilot");
        flight.addLongProperty("CostRent");
        flight.addLongProperty("DeIce");
        flight.addLongProperty("DepCode");
        flight.addLongProperty("DepTime");
        flight.addLongProperty("FlightCode").primaryKey();
        flight.addStringProperty("Autoland");
        flight.addStringProperty("Delay");
        flight.addStringProperty("DeviceCode");
        flight.addStringProperty("FlightDate");
        flight.addStringProperty("FlightNumber");
        flight.addStringProperty("FlightReport");
        flight.addStringProperty("Fuel");
        flight.addStringProperty("FuelUsed");
        flight.addStringProperty("Holdling");
        flight.addStringProperty("LdgDay");
        flight.addStringProperty("LdgNight");
        flight.addStringProperty("LiftSW");
        flight.addStringProperty("LogUTC");
        flight.addStringProperty("minCoP");
        flight.addStringProperty("minDual");
        flight.addStringProperty("minExam");
        flight.addStringProperty("minIFR");
        flight.addStringProperty("minIMT");
        flight.addStringProperty("minInstr");
        flight.addStringProperty("minNight");
        flight.addStringProperty("minPIC");
        flight.addStringProperty("minPICus");
        flight.addStringProperty("minREL");
        flight.addStringProperty("minSFR");
        flight.addStringProperty("minTotal");
        flight.addStringProperty("minU1");
        flight.addStringProperty("minU2");
        flight.addStringProperty("minU3");
        flight.addStringProperty("minU4");
        flight.addStringProperty("minXC");
        flight.addStringProperty("Model");
        flight.addStringProperty("P1");
        flight.addStringProperty("P2");
        flight.addStringProperty("P3");
        flight.addStringProperty("P4");
        flight.addStringProperty("Pax");
        flight.addStringProperty("PF");
        flight.addStringProperty("Reference");
        flight.addStringProperty("Remarks");
        flight.addStringProperty("SignBox");
        flight.addStringProperty("SubModel");
        flight.addStringProperty("timeLDG");
        flight.addStringProperty("timeTO");
        flight.addStringProperty("TODay");
        flight.addStringProperty("TONight");
        flight.addStringProperty("TypeOfApp1");
        flight.addStringProperty("TypeOfApp2");
        flight.addStringProperty("TypeOfApp3");
        flight.addStringProperty("TypeOfInstr");
        flight.addStringProperty("TypeOfLaunch");
        flight.addStringProperty("UserN1");
        flight.addStringProperty("UserN2");
        flight.addStringProperty("UserN3");

        return flight;
    }

    private static Entity addTails(Schema schema) {
        Entity tails = schema.addEntity("Tails");
        tails.addStringProperty("RegAC").primaryKey();
        tails.addStringProperty("CountryCode").notNull();
        return tails;
    }

    private static Entity addCurrencies(Schema schema) {
        Entity currencies = schema.addEntity("Currencies");
        currencies.addIdProperty().primaryKey().autoincrement();
        currencies.addStringProperty("ColorCode");
        currencies.addStringProperty("ExpDate");
        currencies.addStringProperty("Text");
        currencies.addStringProperty("WarnPeriod");
        return currencies;
    }

    *//**
     * Create airfield's Properties
     *
     * @return airfield entity
     *//*
    private static Entity addAirfield(Schema schema) {
        Entity airfield = schema.addEntity("Airfield");
        airfield.addStringProperty("AFCode").primaryKey();
        airfield.addStringProperty("AFICAO");
        airfield.addStringProperty("AFIATA");
        airfield.addStringProperty("AFName");
        airfield.addStringProperty("Latitude");
        airfield.addStringProperty("Longitude");
        airfield.addStringProperty("ElevationFT");
        airfield.addStringProperty("AFCat");
        airfield.addStringProperty("TZCode");
        airfield.addIntProperty("AFCountry");
        airfield.addStringProperty("City");

        return airfield;
    }

    *//**
     * Create CountryGeo's Properties
     *
     * @return CountryGeo entity
     *//*
    private static Entity addCountryGeo(Schema schema) {
        Entity countryGeo = schema.addEntity("CountryGeo");
        countryGeo.addIntProperty("CountryCode").primaryKey();
        countryGeo.addStringProperty("ISO_3166");
        countryGeo.addStringProperty("ISO_Long");
        countryGeo.addStringProperty("ISO_Numeric");
        countryGeo.addStringProperty("CountryName");
        countryGeo.addStringProperty("Capital");
        countryGeo.addStringProperty("Continent");
        countryGeo.addStringProperty("Neighbours");
        countryGeo.addStringProperty("tld");
        countryGeo.addStringProperty("CurrencyCode");
        countryGeo.addStringProperty("CurrencyName");
        countryGeo.addStringProperty("PrefixPhone");
        countryGeo.addStringProperty("PrefixICAO");
        countryGeo.addStringProperty("RegAC");

        return countryGeo;
    }

    *//**
     * Create TimeZone's Properties
     *
     * @return TimeZone entity
     *//*
    private static Entity addTimeZone(Schema schema) {
        Entity timeZone = schema.addEntity("TimeZone");
        timeZone.addStringProperty("TZCode").primaryKey();
        timeZone.addStringProperty("TimeZone");
        timeZone.addStringProperty("InfoDST");
        timeZone.addStringProperty("ZoneAbbrev");
        timeZone.addStringProperty("ZoneName");

        return timeZone;
    }



    *//**
     * Create Airfield's Properties
     *
     * @return Airfield entity
     *//*
    private static Entity addAirfieldPilot(Schema schema) {
        Entity airfield = schema.addEntity("Airfield");
        airfield.addStringProperty("LastApp3");
        airfield.addStringProperty("LastApp2");
        airfield.addStringProperty("LastFlight");
        airfield.addStringProperty("AFCode").primaryKey();
        airfield.addStringProperty("AFName");
        airfield.addStringProperty("AFCountry");
        airfield.addStringProperty("AFIcao");
        airfield.addStringProperty("AFIata");
        airfield.addStringProperty("Latitude");
        airfield.addIntProperty("Longitude");
        airfield.addStringProperty("Notes");
        airfield.addStringProperty("LastApp");
        airfield.addStringProperty("PC");

        return airfield;
    }

    *//**
     * Create DBVersion's Properties
     *
     * @return DBVersion entity
     *//*
    private static Entity addDBVersion(Schema schema) {
        Entity db_version = schema.addEntity("db_version");
        db_version.addStringProperty("version").primaryKey();

        return db_version;
    }

    *//**
     * Create Flight's Properties
     *
     * @return Flight entity
     *//*
    private static Entity addFlightPilot(Schema schema) {
        Entity flight = schema.addEntity("Flight");
        flight.addStringProperty("FlightCode").primaryKey();
        flight.addStringProperty("FlightDate");
        flight.addStringProperty("AircraftCode");
        flight.addStringProperty("DepCode");
        flight.addStringProperty("ArrCode");
        flight.addStringProperty("DepTimeSCH");
        flight.addStringProperty("ArrTimeSCH");
        flight.addStringProperty("TypeOfApp3");
        flight.addStringProperty("TypeOfApp2");
        flight.addStringProperty("minREL");
        flight.addIntProperty("intDepTime");
        flight.addIntProperty("intArrTime");
        flight.addStringProperty("FlightNumber");
        flight.addStringProperty("DepTime");
        flight.addStringProperty("ArrTime");
        flight.addIntProperty("minTotal");
        flight.addIntProperty("minPIC");
        flight.addIntProperty("minPICus");
        flight.addIntProperty("minCoP");
        flight.addIntProperty("minDual");
        flight.addIntProperty("minInstr");
        flight.addIntProperty("minExam");
        flight.addIntProperty("minXC");
        flight.addIntProperty("minNight");
        flight.addIntProperty("minIFR");
        flight.addIntProperty("minSFR");
        flight.addIntProperty("minU1");
        flight.addIntProperty("minU2");
        flight.addIntProperty("minU3");
        flight.addIntProperty("minU4");
        flight.addStringProperty("TODay");
        flight.addStringProperty("TONight");
        flight.addStringProperty("LdgDay");
        flight.addStringProperty("LdgNight");
        flight.addStringProperty("Autoland");
        flight.addStringProperty("Holding");
        flight.addStringProperty("InstrApp");
        flight.addStringProperty("TypeOfApp1");
        flight.addStringProperty("TypeOfLaunch");
        flight.addStringProperty("LiftSW");
        flight.addStringProperty("P1Code");
        flight.addStringProperty("P2Code");
        flight.addStringProperty("P3Code");
        flight.addStringProperty("P4Code");
        flight.addIntProperty("PF");
        flight.addStringProperty("Remarks");
        flight.addStringProperty("FlightReport");
        flight.addStringProperty("SignBox");
        flight.addStringProperty("PC");
        flight.addIntProperty("tot");
        flight.addStringProperty("TypeOfInstr");
        flight.addStringProperty("minIMT");
        flight.addStringProperty("userN1");
        flight.addStringProperty("userN2");
        flight.addStringProperty("userN3");
        flight.addIntProperty("Deice");
        flight.addStringProperty("Delay");
        flight.addStringProperty("Pax");
        flight.addStringProperty("Fuel");
        flight.addStringProperty("FuelUsed");
        flight.addStringProperty("TimeTO");
        flight.addStringProperty("TimeLdg");
        flight.addStringProperty("RecStatus");

        return flight;
    }

    *//**
     * Create Launches's Properties
     *
     * @return Launches entity
     *//*
    private static Entity addLaunches(Schema schema) {
        Entity launches = schema.addEntity("Launches");
        launches.addIntProperty("LaunchCode").primaryKey();
        launches.addStringProperty("LaunchType");

        return launches;
    }

    *//**
     * Create Pilot's Properties
     *
     * @return Pilot entity
     *//*
    private static Entity addPilot(Schema schema) {
        Entity pilot = schema.addEntity("Pilot");
        pilot.addStringProperty("PilotCode").primaryKey();
        pilot.addStringProperty("Company");
        pilot.addStringProperty("PilotRef");
        pilot.addStringProperty("PilotName");
        pilot.addStringProperty("PilotPhone");
        pilot.addStringProperty("PilotEMail");
        pilot.addStringProperty("Notes");
        pilot.addStringProperty("LastFlight");
        pilot.addStringProperty("PC");
        pilot.addIntProperty("sync");
        pilot.addStringProperty("syncPict");
        pilot.addIntProperty("tot");

        return pilot;
    }

    *//**
     * Create Setting's Properties
     *
     * @return Setting entity
     *//*
    private static Entity addSetting(Schema schema) {
        Entity setting = schema.addEntity("Setting");
        setting.addIntProperty("SysCode").primaryKey();
        setting.addStringProperty("Description");
        setting.addStringProperty("Data");
        setting.addBooleanProperty("SyncPC");

        return setting;
    }

    *//**
     * Create Total's Properties
     *
     * @return Totals entity
     *//*
    private static Entity addTotal(Schema schema) {
        Entity totals = schema.addEntity("Totals");
        totals.addIntProperty("totalsID").primaryKey();
        totals.addIntProperty("sumPIC");
        totals.addIntProperty("sumCoP");
        totals.addIntProperty("sumDual");
        totals.addIntProperty("sumPICus");
        totals.addIntProperty("sumInstr");
        totals.addIntProperty("sumExam");
        totals.addIntProperty("sumIFR");
        totals.addIntProperty("sumSFR");
        totals.addIntProperty("sumIMT");
        totals.addIntProperty("sumREL");
        totals.addIntProperty("sumNight");
        totals.addIntProperty("sumXC");
        totals.addIntProperty("sumU1");
        totals.addIntProperty("sumU2");
        totals.addIntProperty("sumU3");
        totals.addIntProperty("sumU4");
        totals.addIntProperty("sumClass1");
        totals.addIntProperty("sumClass2");
        totals.addIntProperty("sumClass3");
        totals.addIntProperty("sumClass4");
        totals.addIntProperty("sumClass5");
        totals.addIntProperty("sumPower0");
        totals.addIntProperty("sumPower1");
        totals.addIntProperty("sumPower2");
        totals.addIntProperty("sumPower3");
        totals.addIntProperty("sumPower4");
        totals.addIntProperty("sumPower5");
        totals.addIntProperty("sumPower6");
        totals.addIntProperty("sumTODay");
        totals.addIntProperty("sumTONight");
        totals.addIntProperty("sumLdgDay");
        totals.addIntProperty("sumLdgNight");
        totals.addIntProperty("sumHolding");
        totals.addIntProperty("sumAutoland");
        totals.addIntProperty("sumACFT");
        totals.addIntProperty("sumSIM");

        return totals;
    }

    *//**
     * Create TypeOfApp's Properties
     *
     * @return TypeOfApp entity
     *//*
    private static Entity addTypeOfApp(Schema schema) {
        Entity typeOfApp = schema.addEntity("TypeOfApp");
        typeOfApp.addIntProperty("Code").primaryKey();
        typeOfApp.addStringProperty("App");
        typeOfApp.addStringProperty("Detail");
        typeOfApp.addStringProperty("Presicion");

        return typeOfApp;
    }

    *//**
     * Create Weather's Properties
     *
     * @return Weather entity
     *//*
    private static Entity addWeather(Schema schema) {
        Entity weather = schema.addEntity("Weather");
        weather.addStringProperty("AFIcao").primaryKey();
        weather.addStringProperty("AFIata");
        weather.addStringProperty("Report");
        weather.addStringProperty("Icon");
        weather.addStringProperty("DateSaved");

        return weather;
    }



    *//**
     * Create user details Properties
     *
     * @return DBUserDetails entity
     *//*
    private static Entity addUserDetails(Schema schema) {
        Entity userDetails = schema.addEntity("DBUserDetails");
        userDetails.addIdProperty().primaryKey().autoincrement();
        //  userDetails.addLongProperty("id").notNull().unique().primaryKey().autoincrement();
        userDetails.addStringProperty("nickName").notNull();
        userDetails.addStringProperty("firstName").notNull();
        userDetails.addStringProperty("lastName").notNull();
        userDetails.addDateProperty("birthDate");
        userDetails.addStringProperty("gender");
        userDetails.addStringProperty("country");
        userDetails.addDateProperty("registrationDate").notNull();
        return userDetails;
    }

    */

    /**
     * Create phone numbers Properties
     *
     * @return DBPhoneNumber entity
     *//*
    private static Entity addPhoneNumber(Schema schema) {
        Entity phone = schema.addEntity("DBPhoneNumber");
        phone.addIdProperty().primaryKey().autoincrement();
        phone.addStringProperty("phoneNumber").notNull().unique();
        return phone;
    }*/
    private static void addTablesPilotLog(Schema schema) {
        //addAircraft(schema);
        //addAirfield(schema);
        //addAllowanceRules(schema);
        //addBackupDB(schema);
        //addDuty(schema);
        //addZExpense(schema);
        //addZExpenseGroup(schema);
        //addQualification(schema);
        //addZQualification(schema);
        addWeatherLocal(schema);
        //addExpense(schema);
        //addFlight(schema);
        //addImagePic(schema);
        //addLimitRules(schema);
        //addPilot(schema);
        //addRecordDelete(schema);
        //addSettingConfig(schema);
        //addSettingLocal(schema);
        //addSettingUserAccount(schema);
        //addValidationRules(schema);
        //addWeather(schema);
        //addWeatherAF(schema);
        //addMyQuery(schema);
        //addMyQueryBuild(schema);
        //addZAircraftMake(schema);
        //addZApproach(schema);
        //addZApproachCat(schema);
        //addZCountry(schema);
        //addZCurrency(schema);
        //addZDelay(schema);
        //addZDelayGroup(schema);
        //addZExpenseCat(schema);
        //addZFNPT(schema);
        //addZLimit(schema);
        //addZLaunch(schema);
        //addZOperation(schema);
        //addZPackage(schema);
        //addZTimeZone(schema);
        //addZTimeZoneDST(schema);
        //addZValidation(schema);

    }

    /**
     * Create Aircraft's Properties
     *
     * @return Aircraft entity
     */
    private static Entity addAircraft(Schema schema) {
        Entity aircraft = schema.addEntity("Aircraft");
        aircraft.addByteArrayProperty("AircraftCode").primaryKey();
        aircraft.addBooleanProperty("Active");//1
        aircraft.addIntProperty("DeviceCode");
        aircraft.addStringProperty("Company");
        aircraft.addStringProperty("Reference");
        aircraft.addStringProperty("RefSearch");
        aircraft.addStringProperty("Fin");
        aircraft.addStringProperty("Make");
        aircraft.addStringProperty("Model");
        aircraft.addStringProperty("SubModel");
        aircraft.addStringProperty("Rating");
        aircraft.addIntProperty("Category");
        aircraft.addIntProperty("Class");
        aircraft.addBooleanProperty("Sea");//0
        aircraft.addIntProperty("Power");
        aircraft.addBooleanProperty("Kg5700"); //0
        aircraft.addBooleanProperty("TMG"); //0
        aircraft.addBooleanProperty("Tailwheel"); //0
        aircraft.addBooleanProperty("Complex"); //0
        aircraft.addBooleanProperty("HighPerf"); //0
        aircraft.addBooleanProperty("Aerobatic"); //0
        aircraft.addIntProperty("Seats");
        aircraft.addIntProperty("FNPT");
        aircraft.addIntProperty("DefaultApp");
        aircraft.addIntProperty("DefaultLaunch");
        aircraft.addIntProperty("DefaultOps");
        aircraft.addIntProperty("DefaultLog");
        aircraft.addIntProperty("CondLog");
        aircraft.addBooleanProperty("Run2"); //0
        aircraft.addLongProperty("Record_Modified");
        aircraft.addBooleanProperty("Record_Upload"); //1
        return aircraft;
    }

    private static Entity addAirfield(Schema schema) {
        Entity airfield = schema.addEntity("Airfield");
        airfield.addByteArrayProperty("AFCode").primaryKey();
        airfield.addStringProperty("AFICAO");
        airfield.addStringProperty("AFIATA");
        airfield.addStringProperty("AFFAA");
        airfield.addStringProperty("AFName");
        airfield.addIntProperty("Latitude");
        airfield.addIntProperty("Longitude");
        airfield.addIntProperty("AFCountry");
        airfield.addIntProperty("AFCat");
        airfield.addIntProperty("ElevationFT");
        airfield.addIntProperty("TZCode");
        airfield.addStringProperty("City");
        airfield.addStringProperty("Notes");
        airfield.addStringProperty("NotesUser");
        airfield.addBooleanProperty("ShowList");
        airfield.addBooleanProperty("UserEdit");
        airfield.addLongProperty("Record_Modified");
        airfield.addBooleanProperty("Record_Upload");//1
        return airfield;
    }

    private static Entity addAllowanceRules(Schema schema) {
        Entity allowanceRules = schema.addEntity("AllowanceRules");
        allowanceRules.addByteArrayProperty("ARCode").primaryKey();
        allowanceRules.addIntProperty("ARType");
        allowanceRules.addIntProperty("ARClassEngine");
        allowanceRules.addStringProperty("ARModel");
        allowanceRules.addStringProperty("ARSubModel");
        allowanceRules.addIntProperty("ARCurrency");//0
        allowanceRules.addIntProperty("ARCostFix");//0
        allowanceRules.addIntProperty("ARCostVar");//0
        allowanceRules.addIntProperty("ARCostMinHour");//0
        allowanceRules.addIntProperty("ARCostBase");//0
        allowanceRules.addIntProperty("ARWhen");//0
        allowanceRules.addStringProperty("ARWhen1");
        allowanceRules.addStringProperty("ARWhen2");
        allowanceRules.addStringProperty("ARReadable");//0
        allowanceRules.addLongProperty("Record_Modified");
        allowanceRules.addBooleanProperty("Record_Upload");//1
        return allowanceRules;
    }

    private static Entity addBackupDB(Schema schema) {
        Entity entity = schema.addEntity("BackupDB");
        entity.addByteArrayProperty("BackupCode").primaryKey();
        entity.addStringProperty("DateTime");
        entity.addIntProperty("FlightRecords");
        entity.addStringProperty("dbURL");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addDuty(Schema schema) {
        Entity entity = schema.addEntity("Duty");
        entity.addByteArrayProperty("DutyCode").primaryKey();
        entity.addIntProperty("DutyType");
        entity.addBooleanProperty("Deadhead");
        entity.addStringProperty("EventDescription");
        entity.addStringProperty("EventDateUTC");
        entity.addStringProperty("EventDateLOCAL");
        entity.addStringProperty("EventDateBASE");
        entity.addIntProperty("EventStartUTC");
        entity.addIntProperty("EventEndUTC");
        entity.addIntProperty("StartOffset");
        entity.addIntProperty("EndOffset");
        entity.addIntProperty("BaseOffset");
        entity.addIntProperty("StartTZCode");
        entity.addIntProperty("EndTZCode");
        entity.addIntProperty("Duration");
        entity.addStringProperty("DutyNotes");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addExpense(Schema schema) {
        Entity entity = schema.addEntity("Expense");
        entity.addByteArrayProperty("ExpCode").primaryKey();
        entity.addStringProperty("ExpDate");
        entity.addIntProperty("ETCode");//0
        entity.addStringProperty("Description");
        entity.addLongProperty("Amount");
        entity.addIntProperty("CurrCode"); //0
        entity.addLongProperty("AmountForeign");
        entity.addIntProperty("CurrCodeForeign"); //0
        entity.addIntProperty("LinkTable");
        entity.addByteArrayProperty("LinkCode");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    //change
    private static Entity addFlight(Schema schema) {
        Entity entity = schema.addEntity("Flight");
        entity.addByteArrayProperty("FlightCode").primaryKey();
        entity.addStringProperty("DateUTC");
        entity.addStringProperty("DateLOCAL");
        entity.addStringProperty("DateBASE");
        entity.addStringProperty("FlightNumber");
        entity.addStringProperty("Pairing");
        entity.addByteArrayProperty("AircraftCode");
        entity.addByteArrayProperty("DepCode");
        entity.addByteArrayProperty("ArrCode");
        entity.addStringProperty("DepRwy");
        entity.addStringProperty("ArrRwy");
        entity.addIntProperty("DepTimeUTC");//0
        entity.addIntProperty("ArrTimeUTC");//0
        entity.addIntProperty("DepTimeSCHED");//0
        entity.addIntProperty("ArrTimeSCHED");//0
        entity.addIntProperty("ToTimeUTC");//0
        entity.addIntProperty("LdgTimeUTC");//0
        entity.addIntProperty("DepOffset");//0
        entity.addIntProperty("ArrOffset");//0
        entity.addIntProperty("BaseOffset");//0
        entity.addIntProperty("minTOTAL");//0
        entity.addIntProperty("minAIR");//0
        entity.addIntProperty("minPIC");//0
        entity.addIntProperty("minPICUS");//0
        entity.addIntProperty("minCOP");//0
        entity.addIntProperty("minDUAL");//0
        entity.addIntProperty("minINSTR");//0
        entity.addIntProperty("minEXAM");//0
        entity.addIntProperty("minREL");//0
        entity.addIntProperty("minNIGHT");//0
        entity.addIntProperty("minXC");//0
        entity.addIntProperty("minIFR");//0
        entity.addIntProperty("minIMT");//0
        entity.addIntProperty("minSFR");//0
        entity.addIntProperty("minU1");//0
        entity.addIntProperty("minU2");//0
        entity.addIntProperty("minU3");//0
        entity.addIntProperty("minU4");//0
        entity.addIntProperty("ToDay");//0
        entity.addIntProperty("ToNight");//0
        entity.addIntProperty("LdgDay");//0
        entity.addIntProperty("LdgNight");//0
        entity.addIntProperty("Holding");//0
        entity.addIntProperty("LiftSW");//0
        entity.addIntProperty("Fuel");//0
        entity.addIntProperty("FuelUsed");//0
        entity.addIntProperty("FuelPlanned");//0
        entity.addIntProperty("Pax");//0
        entity.addBooleanProperty("DeIce");//0
        entity.addIntProperty("UserNum");//0
        entity.addStringProperty("UserText");
        entity.addBooleanProperty("UserBool");//0
        entity.addStringProperty("TagApproach");
        entity.addStringProperty("TagLaunch");
        entity.addStringProperty("TagOps");
        entity.addStringProperty("TagDelay");
        entity.addBooleanProperty("PF");//0
        entity.addByteArrayProperty("P1Code");
        entity.addByteArrayProperty("P2Code");
        entity.addByteArrayProperty("P3Code");
        entity.addByteArrayProperty("P4Code");
        entity.addStringProperty("CrewList");
        entity.addStringProperty("Training");
        entity.addStringProperty("Remarks");
        entity.addStringProperty("Report");
        entity.addIntProperty("SignBox");//0
        entity.addBooleanProperty("NextPage");//0
        entity.addBooleanProperty("NextSummary");//0
        entity.addBooleanProperty("ToEdit");//0
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addImagePic(Schema schema) {
        Entity entity = schema.addEntity("ImagePic");
        entity.addByteArrayProperty("ImgCode").primaryKey();
        entity.addByteArrayProperty("LinkCode");
        entity.addStringProperty("FileName");
        entity.addStringProperty("ImgURL");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addLimitRules(Schema schema) {
        Entity entity = schema.addEntity("LimitRules");
        entity.addByteArrayProperty("LimitCode").primaryKey();
        entity.addIntProperty("LType");
        entity.addIntProperty("LMinutes");
        entity.addIntProperty("LPeriod");
        entity.addStringProperty("LFrom");
        entity.addStringProperty("LTo");
        entity.addIntProperty("LZone");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addPilot(Schema schema) {
        Entity entity = schema.addEntity("Pilot");
        entity.addByteArrayProperty("PilotCode").primaryKey();
        entity.addBooleanProperty("Active");//1
        entity.addStringProperty("Company");
        entity.addStringProperty("PilotRef");
        entity.addStringProperty("PilotName");
        entity.addStringProperty("PilotSearch");
        entity.addStringProperty("PilotPhone");
        entity.addStringProperty("PhoneSearch");
        entity.addStringProperty("PilotEMail");
        entity.addStringProperty("Facebook");
        entity.addStringProperty("LinkedIn");
        entity.addStringProperty("Certificate");
        entity.addStringProperty("Notes");
        entity.addStringProperty("RosterAlias");
        entity.addStringProperty("airCREWaccount");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addRecordDelete(Schema schema) {
        Entity entity = schema.addEntity("RecordDelete");
        entity.addStringProperty("TableName");
        entity.addByteArrayProperty("RecordCode").primaryKey();
        return entity;
    }

    private static Entity addSettingConfig(Schema schema) {
        Entity entity = schema.addEntity("SettingConfig");
        entity.addIntProperty("ConfigCode").primaryKey();
        entity.addStringProperty("Group");
        entity.addStringProperty("Name");
        entity.addStringProperty("Data");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addSettingLocal(Schema schema) {
        Entity entity = schema.addEntity("SettingLocal");
        entity.addIntProperty("ConfigCode").primaryKey();
        entity.addStringProperty("Group");
        entity.addStringProperty("Name");
        entity.addStringProperty("Data");
        return entity;
    }

    //change
    private static Entity addSettingUserAccount(Schema schema) {
        Entity entity = schema.addEntity("SettingUserAccount");
        entity.addIntProperty("ConfigCode").primaryKey();
        entity.addStringProperty("Group");
        entity.addStringProperty("Name");
        entity.addStringProperty("Data");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addValidationRules(Schema schema) {
        Entity entity = schema.addEntity("ValidationRules");
        entity.addByteArrayProperty("VCode").primaryKey();
        entity.addIntProperty("VTCode");
        entity.addStringProperty("VDate");
        entity.addIntProperty("VDays");
        entity.addStringProperty("VModel");
        entity.addStringProperty("VComment");
        entity.addIntProperty("VLanding");
        entity.addIntProperty("VLandingPeriod");
        entity.addIntProperty("VCCT");
        entity.addIntProperty("VExtra");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addWeather(Schema schema) {
        Entity entity = schema.addEntity("Weather");
        entity.addByteArrayProperty("WxCode").primaryKey();
        entity.addStringProperty("WxRoute");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addWeatherAF(Schema schema) {
        Entity entity = schema.addEntity("WeatherAF");
        entity.addByteArrayProperty("WxAFCode").primaryKey();
        entity.addByteArrayProperty("WxCode");
        entity.addByteArrayProperty("AFCode");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addMyQuery(Schema schema) {
        Entity entity = schema.addEntity("MyQuery");
        entity.addByteArrayProperty("mQCode").primaryKey();
        entity.addStringProperty("ShortName");
        entity.addStringProperty("Name");
        entity.addBooleanProperty("QuickView");//0
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addMyQueryBuild(Schema schema) {
        Entity entity = schema.addEntity("MyQueryBuild");
        entity.addByteArrayProperty("mQBCode").primaryKey();
        entity.addByteArrayProperty("mQCode");
        entity.addStringProperty("Build1");
        entity.addIntProperty("Build2");//0
        entity.addIntProperty("Build3");//0
        entity.addStringProperty("Build4");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    private static Entity addZAircraftMake(Schema schema) {
        Entity entity = schema.addEntity("ZAircraftMake");
        entity.addIntProperty("ACCode").primaryKey();
        entity.addStringProperty("ACRef");
        entity.addStringProperty("Make");
        entity.addStringProperty("Model");
        entity.addStringProperty("SubModel");
        entity.addStringProperty("Rating");
        entity.addIntProperty("Category");
        entity.addIntProperty("Class");
        entity.addBooleanProperty("Sea");
        entity.addIntProperty("Power");
        entity.addBooleanProperty("Kg5700");
        entity.addBooleanProperty("TMG");
        entity.addBooleanProperty("Tailwheel");
        entity.addBooleanProperty("Complex");
        entity.addBooleanProperty("HighPerf");
        entity.addBooleanProperty("Aerobatic");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    //change
    private static Entity addZApproach(Schema schema) {
        Entity entity = schema.addEntity("ZApproach");
        entity.addIntProperty("APCode").primaryKey();
        entity.addIntProperty("APCat");
        entity.addStringProperty("APShort");
        entity.addStringProperty("APLong");
        entity.addIntProperty("MostUsed");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    //change
    private static Entity addZApproachCat(Schema schema) {
        Entity entity = schema.addEntity("ZApproachCat");
        entity.addIntProperty("APCat").primaryKey();
        entity.addStringProperty("APCatShort");
        entity.addStringProperty("APCatLong");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    private static Entity addZCountry(Schema schema) {
        Entity entity = schema.addEntity("ZCountry");
        entity.addIntProperty("CountryCode").primaryKey();
        entity.addStringProperty("ISO_3166");
        entity.addStringProperty("ISO_Long");
        entity.addLongProperty("ISO_Numeric");
        entity.addStringProperty("CountryName");
        entity.addStringProperty("Capital");
        entity.addStringProperty("Continent");
        entity.addStringProperty("Neighbours");
        entity.addStringProperty("tld");
        entity.addIntProperty("CurrCode");
        entity.addStringProperty("PrefixPhone");
        entity.addStringProperty("PrefixICAO");
        entity.addStringProperty("RegAC");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    //change
    private static Entity addZCurrency(Schema schema) {
        Entity entity = schema.addEntity("ZCurrency");
        entity.addIntProperty("CurrCode").primaryKey();
        entity.addStringProperty("CurrShort");
        entity.addStringProperty("CurrLong");
        entity.addLongProperty("ConversionRate");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    private static Entity addZDelay(Schema schema) {
        Entity entity = schema.addEntity("ZDelay");
        entity.addIntProperty("DelayCode").primaryKey();
        entity.addStringProperty("DelayDD");
        entity.addStringProperty("DelayShort");
        entity.addStringProperty("DelayLong");
        entity.addIntProperty("DelayGroupCode");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    private static Entity addZDelayGroup(Schema schema) {
        Entity entity = schema.addEntity("ZDelayGroup");
        entity.addIntProperty("DelayGroupCode").primaryKey();
        entity.addStringProperty("DelayGG");
        entity.addStringProperty("DelayGroupName");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    //change
    private static Entity addZExpense(Schema schema) {
        Entity entity = schema.addEntity("ZExpense");
        entity.addIntProperty("ETCode").primaryKey();
        entity.addBooleanProperty("Debit");//0
        entity.addStringProperty("ExpTypeShort");
        entity.addStringProperty("ExpTypeLong");
        entity.addIntProperty("ExpGroupCode");
        entity.addIntProperty("MostUsed");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    private static Entity addZExpenseGroup(Schema schema) {
        Entity entity = schema.addEntity("ZExpenseGroup");
        entity.addIntProperty("ExpGroupCode").primaryKey();
        entity.addStringProperty("ExpGroupShort");
        entity.addStringProperty("ExpGroupLong");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    //change
    private static Entity addZQualification(Schema schema) {
        Entity entity = schema.addEntity("ZQualification");
        entity.addIntProperty("QTypeCode").primaryKey();
        entity.addStringProperty("QTypeLong");
        entity.addBooleanProperty("IsCertificate");
        entity.addStringProperty("MinimumWord1");
        entity.addStringProperty("MinimumWord2");
        entity.addBooleanProperty("ReqModel");
        entity.addBooleanProperty("ReqAirfield");
        entity.addBooleanProperty("ReqExtra");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    //change
    private static Entity addQualification(Schema schema) {
        Entity entity = schema.addEntity("Qualification");
        entity.addByteArrayProperty("QCode").primaryKey();
        entity.addIntProperty("QTypeCode");
        entity.addIntProperty("Validity");
        entity.addStringProperty("DateIssued");
        entity.addStringProperty("DateValid");
        entity.addIntProperty("MinimumQty");
        entity.addIntProperty("MinimumPeriod");
        entity.addStringProperty("RefModel");
        entity.addByteArrayProperty("RefAirfield");
        entity.addIntProperty("RefExtra");
        entity.addIntProperty("NotifyDays");
        entity.addStringProperty("NotifyComment");
        entity.addLongProperty("Record_Modified");
        entity.addBooleanProperty("Record_Upload");//1
        return entity;
    }

    //change
    private static Entity addWeatherLocal(Schema schema) {
        Entity entity = schema.addEntity("WeatherLocal");
        entity.addStringProperty("AFIcao").primaryKey();
        entity.addStringProperty("AFIata");
        entity.addStringProperty("Report");
        entity.addStringProperty("Icon");
        entity.addStringProperty("DataSaved");
        entity.addStringProperty("AFName");
        entity.addStringProperty("AFCountry");
        return entity;
    }

    //change
    private static Entity addZFNPT(Schema schema) {
        Entity entity = schema.addEntity("ZFNPT");
        entity.addIntProperty("FnptCode").primaryKey();
        entity.addStringProperty("FnptShort");
        entity.addStringProperty("FnptLong");
        entity.addBooleanProperty("Drone");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    //change
    private static Entity addZLimit(Schema schema) {
        Entity entity = schema.addEntity("ZLimit");
        entity.addIntProperty("LPeriodCode").primaryKey();
        entity.addStringProperty("LPeriodLong");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    //change
    private static Entity addZLaunch(Schema schema) {
        Entity entity = schema.addEntity("ZLaunch");
        entity.addIntProperty("LaunchCode").primaryKey();
        entity.addStringProperty("LaunchShort");
        entity.addStringProperty("LaunchLong");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    //change
    private static Entity addZOperation(Schema schema) {
        Entity entity = schema.addEntity("ZOperation");
        entity.addIntProperty("OpsCode").primaryKey();
        entity.addStringProperty("OpsShort");
        entity.addStringProperty("OpsLong");
        entity.addIntProperty("MostUsed");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    private static Entity addZPackage(Schema schema) {
        Entity entity = schema.addEntity("ZPackage");
        entity.addIntProperty("PackageCode").primaryKey();
        entity.addStringProperty("PackID");
        entity.addStringProperty("Table");
        entity.addStringProperty("Details");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    //change
    private static Entity addZTimeZone(Schema schema) {
        Entity entity = schema.addEntity("ZTimeZone");
        entity.addIntProperty("TZCode").primaryKey();
        entity.addStringProperty("TimeZone");
        entity.addStringProperty("ZoneShort");
        entity.addStringProperty("ZoneLong");
        entity.addStringProperty("DSTCode");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    private static Entity addZTimeZoneDST(Schema schema) {
        Entity entity = schema.addEntity("ZTimeZoneDST");
        entity.addStringProperty("DSTCode").primaryKey();
        entity.addStringProperty("Rule");
        entity.addLongProperty("Record_Modified");
        return entity;
    }

    private static Entity addZValidation(Schema schema) {
        Entity entity = schema.addEntity("ZValidation");
        entity.addIntProperty("VTCode").primaryKey();
        entity.addStringProperty("VTerm");
        entity.addLongProperty("Record_Modified");
        return entity;
    }
}
