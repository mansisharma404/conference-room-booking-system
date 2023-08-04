import java.util.*;
public class ConferenceRoomBookingSystem {

    static  TreeMap<String,Building> buildingMap;
    static TreeMap<String,ConferenceRoom> conferenceRoomMapping;
    static List<Bookings> bookingsList;
    static String ADD_BUILDING_ERROR = " Oops, cannot make building with this name its already taken please try another name";
    static String INVALID_BUILDING = " Oops, building name is invalid";
    static String INVALID_TIMESTAMP = " Oops, innvalid timestamp provided";
    static String ALREADY_BOOKED = " Oops, the room is already booked. Try again later or book some other room";

    static String INVALID_CONFERENCE_ROOM = " Oops, conference room name is invalid";
    static String NO_BUILDING_FOUND_ERROR = " Oops, no building with this name can be found please enter valid building name";
    static String FLOOR_ALREADY_EXISTS = " Oops, floor with similar name in building already exists.";
    public static class Building {
        private String buildingName;
        private List<String> floors = null;

        Building(String name) {
            buildingName = name;
            floors = new ArrayList<String>();
        }

        public static void printAllBuildings() {
            for (Map.Entry<String, Building> entry : buildingMap.entrySet()) {
                System.out.println(entry.getValue().buildingName);
            }
        }

        public static String createBuilding(String name) {
            String uppercaseName = name.toUpperCase();
            if (buildingMap.get(uppercaseName) != null) return ADD_BUILDING_ERROR;
            Building newBuild = new Building(uppercaseName);
            buildingMap.put(uppercaseName, newBuild);
            return uppercaseName;
        }
        public static Building  getBuildingIfValidBuildingAndFloorName(String buildingname, String floor) throws Exception{
            String buildingnameUpperCase = buildingname.toUpperCase();
            String floornameUpperCased = floor.toUpperCase();
            ConferenceRoomBookingSystem.Building building = buildingMap.get(buildingnameUpperCase);
            if (building == null) throw new Exception(NO_BUILDING_FOUND_ERROR) ;
            for(int i=0;i<building.floors.size();i++){
                String curFloorName = building.floors.get(i);
                if(Objects.equals(curFloorName, floornameUpperCased))throw new Exception(FLOOR_ALREADY_EXISTS);
            }
            return building;
        }
        public static String addFloor(String buildingname, String floor) throws  Exception{
                Building building = getBuildingIfValidBuildingAndFloorName(buildingname,floor);
                building.floors.add(floor.toUpperCase());
                return "Successfully added floor - " + floor.toUpperCase() + "to building - " + buildingname.toUpperCase();
        }
    }
    public static class Bookings{
        Integer startTime;
        Integer endTime;
        Boolean isActive;
        String conferenceRoomId;
        Bookings(int start,int end,String conferenceRoom){
            startTime = start;
            endTime = end;
            conferenceRoomId = conferenceRoom;
            isActive = true;
        }
        public static Bookings bookRoom(int start,int end,String conferenceRoom, String building,String floor)throws Exception{
            Building curBuilding =  Building.getBuildingIfValidBuildingAndFloorName(building,floor);
            String Id = conferenceRoom.toUpperCase() + "_" + curBuilding.buildingName + "_" + floor.toUpperCase();
            ConferenceRoom cRoom = conferenceRoomMapping.get(Id);
            if(cRoom == null)throw new Exception(INVALID_CONFERENCE_ROOM);
            if(start < 0 || start > 24 || end < 0 || end > 24 || end > start)throw new Exception(INVALID_TIMESTAMP);
            List<Bookings> bookings = cRoom.bookings;
            for(int i=0;i<bookings.size();i++){
                if(bookings.get(i).startTime < start && bookings.get(i).endTime > start && bookings.get(i).isActive  )throw new Exception(ALREADY_BOOKED);
                if(bookings.get(i).startTime < end && bookings.get(i).endTime > end && bookings.get(i).isActive )throw new Exception(ALREADY_BOOKED);
            }
            Bookings booking = new Bookings(start,end,cRoom._id);
            bookingsList.add(booking);
            return booking;
        }
    }
    public static class ConferenceRoom{
        private String _id;
        private String name;
        private String buildingId;
        private String floor;
        private List<Bookings> bookings = null;
        ConferenceRoom(String roomName, String buildingName,String floorName){
            _id = roomName+"_"+buildingName+"_"+floorName;
            name = roomName;
            buildingId = buildingName;
            floor = floorName;
        }
        public static ConferenceRoom addConferenceRoom(String roomName, String buildingName,String floorName) throws Exception{
            Building building =  Building.getBuildingIfValidBuildingAndFloorName(buildingName,floorName);
            ConferenceRoom c = new ConferenceRoom(roomName.toUpperCase(),buildingName.toUpperCase(),floorName.toUpperCase());
            conferenceRoomMapping.put(c._id,c);
            return c;
        }
   }
    public static void main(String[] args){
        System.out.println("Conference room booking system");
        buildingMap = new TreeMap<>();
        conferenceRoomMapping = new TreeMap<>();
        bookingsList = new ArrayList<>();

        while(true){
            Scanner stringScanner = new Scanner(System.in);
            String command = stringScanner.nextLine();

            if(Objects.equals(command, "ADD BUILDING")){
                System.out.println("Enter the name of building");
                String buildingname = stringScanner.nextLine();
                String res = Building.createBuilding(buildingname);
                System.out.println(res);
            }

            else if(Objects.equals(command, "PRINT BUILDING")){
                Building.printAllBuildings();
            }

            if(Objects.equals(command, "ADD FLOOR")){
                System.out.println("Enter the name of building in which you want to end floor");
                String buildingName = stringScanner.nextLine();
                System.out.println("Enter unique floor name");
                String floorname = stringScanner.nextLine();
                try{
                    String res = Building.addFloor(buildingName, floorname);
                    System.out.println(res);
                }catch(Exception e) {
                    System.out.println(e);
                }
            }

        }
    }
}
