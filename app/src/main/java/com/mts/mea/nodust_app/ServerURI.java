package com.mts.mea.nodust_app;
public class ServerURI {
  // public static final String BASE_URL = "http://192.168.0.138:7101/NoDustWS/resources/NoDustWS";
 // public static final String BASE_URL = "http://192.168.0.99:7001/NoDustWS/resources/NoDustWS";
  //// public static final String BASE_URL = "http://197.45.198.190:7001/NoDustWS/resources/NoDustWS";
   //public static final String BASE_URL = "http://192.168.43.91:82/NoDust_1.0/NoDust";
 // public static final String BASE_URL = "http://www.nodust-eg.com/dms";
//public static final String BASE_URL = "http://192.168.0.200:82/NoDust_1.0/NoDust";
//public static final String BASE_URL = "http://gdms.nodust-eg.com:80/NoDust_1.0/NoDust";
//public static final String BASE_URL = "http://gdms.nodust-eg.com:80/NoDust_Prod/NoDust";
   // public static final String BASE_URL = "http://gdms.nodust-eg.com:80/NoDust";
public static final String BASE_URL = "http://gdms.nodust-eg.com:80/NoDust_Dev/NoDust";



    public static String getLoginURL()
    {
        String url=BASE_URL+"/LogIn";
        return url;
    }
    public static String ViewAssignedTasks()
    {
        String url=BASE_URL+"/ViewAssignmentListPilot";
        return url;
    }
    public static String AssignTask()
    {
        String url=BASE_URL+"/AssignTask";
        return url;
    }
    public static String getTotalAmount()
    {
        String url=BASE_URL+"/GetTotalAmount";
        return url;
    }
    public static String ViewProducts()
    {
        String url=BASE_URL+"/GetProductsPilot";
        return url;
    }
    public static String GetTasksNotAssig()
    {
        String url=BASE_URL+"/ViewAssignmentList";
        return url;
    }
    public static String GetKPI()
    {
        String url=BASE_URL+"/GetKPI";
        return url;
    }
    public static String ViewProductsDriver()
    {
        String url=BASE_URL+"/GetProductDriver";
        return url;
    }
    public static String ViewCoverProducts()
    {
        String url=BASE_URL+"/GetCoverProduct";
        return url;
    }
    public static String ViewAssignmentDetails()
    {
        String url=BASE_URL+"/ViewAssignmentDetails";
        return url;
    }
    public static String GetCloseCodesGroups()
    {
        String url=BASE_URL+"/GetCloseCodesGroups";
        return url;
    }
    public static String GetCloseCodes()
    {
        String url=BASE_URL+"/GetCloseCodes";
        return url;
    }
    public static String PilotReconcilation()
    {
        String url=BASE_URL+"/PilotReconcilation";
        return url;
    }
    public static String DriverReconcilation()
    {
        String url=BASE_URL+"/DriverReconcilation";
        return url;
    }
    public static String PilotRejectCover()
    {
        String url=BASE_URL+"/PilotRejectCover";
        return url;
    }

    public static String SetAssignmentAction()
    {
        String url=BASE_URL+"/SetAssignmentAction";
        return url;
    }
    public static String StartShift()
    {
        String url=BASE_URL+"/StratShift";
        return url;
    }
    public static String SetAttendance()
    {
        String url=BASE_URL+"/SetAttendancePilot";
        return url;
    }
    public static String SetReturnTimePilot()
    {
        String url=BASE_URL+"/SetReturnTimePilot";
        return url;
    }
    public static String SetEvalution()
    {
        String url=BASE_URL+"/SetEvalution";
        return url;
    }
    public static String EndShift()
    {
        String url=BASE_URL+"/EndShift";
        return url;
    }
    public  static String sendSMS()
    {
        String url=BASE_URL+"/SendSms";
        return url;
    }
    public  static String PilotCover()
    {
        String url=BASE_URL+"/GetCoverPilots";
        return url;
    }
    public  static String GetCoverForAllPilots()
    {
        String url=BASE_URL+"/GetCoverForAllPilots";
        return url;
    }
    public  static String GetCoverForPilot()
    {
        String url=BASE_URL+"/GetCoverForPilot";
        return url;
    }
    public  static String AddCoverToPilot()
    {
        String url=BASE_URL+"/AddCoverToPilot";
        return url;
    }
    public  static String CheckCoverPilotProduct()
    {
        String url=BASE_URL+"/CheckCoverPilotProduct";
        return url;
    }

    public  static String PilotAcceptCover()
    {
        String url=BASE_URL+"/PilotAcceptCover";
        return url;
    }

    public static String TodayOutputDriver()
    {
        String url=BASE_URL+"/SetTotalAmount_RecNo_driver";
        return url;
    }
    public static String TodayOutputPilot()
    {
        String url=BASE_URL+"/SetTotalAmount_RecNo_pilot";
        return url;
    }
    public static String GetPackagesDetails()
    {
        String url=BASE_URL+"/GetPackagesDetails";
        return url;
    }
    public static String GetProductsReference()
    {
        String url=BASE_URL+"/GetProductsrefernce";
        return url;
    }

    public static String SaveVacation() {
        String url=BASE_URL+"/SaveVacation";
        return url;
    }

    public static String GetVacations() {
        String url=BASE_URL+"/GetALLVacation";
        return url;
    }

    public static String GetVacationReasons() {
        String url=BASE_URL+"/GetVacationReasons";
        return url;
    }

    public static String changePassword() {
        String url=BASE_URL+"/ChangePassword";
        return url;
    }
    public static String GetCollectionPilot() {
        String url=BASE_URL+"/GetCollectionPilot";
        return url;
    }
    public static String GetCollectionDriver() {
        String url=BASE_URL+"/GetCollectionDriver";
        return url;
    }

    public static String GetReconciliationRequest() {
        String url=BASE_URL+"/GetReconciliationRequest";
        return url;
    }
}
