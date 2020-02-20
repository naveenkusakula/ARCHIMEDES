# Application name: Archimedes
## Team: Phoenix

| Team Member | Banner id | Email id |
| ------ | ------ | ------ |
| Dhruv Purohit | B00821734 | dh613935@dal.ca |
| Varun Mahagaokar | B00826634 | vr616271@da.ca |
| Nishant Bhambhani | B00823348 | ns372055@dal.ca |
| Naveen Kusakula | B00781205 | nv633449@dal.ca |

# Git Repository

Please click on [GitLab](https://git.cs.dal.ca/kusakula/pheonix.git) where the code is located.

# Project Summary

The Archimedes Principle can be stated as - when a heavy object sinks into the water in a bathtub, the amount of water rising in a bathtub is equal to the weight of the object that has sunk into the water. We pulled the idea of this principle to implement an android application that balances a student's academic routine with his/her recreational activities. Archimedes suggests the leisure time for activities that a student is interested in by analyzing one's academic schedule. The android application suggests the user to get indulged into the recreational activities by entering one's interest and the application will suggest the time slots for the same. The key features of this application are: The schedule can be accessed from any device as it uses the google sign in API; the application locates the user's current location and the activity location; the duration to reach to the activity hub will also be shown using google maps API, which will help the user to calculate the time required to invest for a particular activity. The activity time slot suggested by the application can also be accepted or rejected by the user.

## Libraries

*Volley 1.1.0:* Volley 1.1.0 is an HTTP library used to send simple GET and POST requests to API.

*Google Play Services auth 15.0.1:* Google Play Services auth 15.0.1 is used for user authentication and login.

*Google Play. Services Maps 15.0.0:* Google Map Services are used to get the directions and routes to Activites location.

*v7 recyclerview:* Recycler view is an advanced version of ListView, which is used to display the list of activities and class schedule.

*Google gson 2.4:* Google gson library is used for parsing JSON objects

## Installation Notes

The application does not require any additional steps for installation, it follows the normal Android application installation process.

Minimum Andriod Version Supported: Andriod Marshmallow

Applications Database:  The Application(Java Spring Boot Application) which provides API to perform CRUD operations with the database is hosted on AWS, so that the API's are accessed from all the running instances of the mobile applications. The AWS instance is stopped as of now. In order to get the data
- Please Mail any of the project members before testing the application 
- Please change the IP address XXXXXXXXXXX in  app/src/main/res/values/strings.xml with the IP address provided by the project team member in response to your email.

    <string name="ipAddress">XXXXXXXXXXXX</string>

## Code Examples

**Problem 1: We required to calculate the free timeslots of the user, considering the occupied timeslots of the user**

For calculating the free timeslots for a day of the user, so we had to fetch the schedule list of the user, from which we extracted the occupied timeslots for a single day.
Then accordingly we calculated the free timeslot using the code provided below :


// The method we implemented that solved our problem
```
public Map<Integer, String> schedule(ArrayList<Integer> classschedule, Map<String, Integer> activites) {

    Set<Entry<Integer, String>> st = slots.entrySet();

    for (Entry<Integer, String> me : st) {
        for (int i = 0; i < classschedule.size(); i++) {
            Integer key = me.getKey();
            int cls = classschedule.get(i);
            if (cls == key) {
                slots.put(key, "schedulded");
            }

        }

    }

    ArrayList<Integer> freeslots = new ArrayList<Integer>();
    for (Entry<Integer, String> me : st) {

        String val = me.getValue();
        if (val.equals("free")) {
            int key = me.getKey();
            freeslots.add(key);
        }
    }


    Set<Entry<String, Integer>> st1 = activites.entrySet();

    for (Entry<String, Integer> me : st1) {

        String key = me.getKey();
        int hrs = me.getValue();
        if (hrs > 0) {
            for (int k = 0; k < hrs; k++) {
                try {
                    int slot = freeslots.get(0);
                    slots.put(slot, key);
                    activites.put(key, hrs - 1);
                    freeslots.remove(0);
                } catch (Exception e) {
                    break;
                }
            }
        }
    }
    return slots;
}
```
**Problem 2: We had to provide choice to user for accepting or rejecting the suggestions provided by the application for extra activites in user's free timeslots**

After assigning the timeslot to user's extra activities, We need to provide user options for accepting or rejecting the assigned timeslot for preferred activities. To overcome this problem, we provided the user with two buttons one for accepting and another for rejecting.
Then accordingly we calculated the free timeslot using the code provided below :


// The method we implemented that solved our problem

```

public MyViewHolder(View view) {
            super(view);
           
            btnAccept = (Button)view.findViewById(R.id.btnAccept);
            btnReject = (Button) view.findViewById(R.id.btnReject);
           
            mContext = view.getContext();
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                       // System.out.println("--------clicked" + scheduleList.get(getAdapterPosition()).getScheduleId());
                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                        JSONObject sl = new JSONObject();
                       // set the values in json object using getter setter methods.
                        JsonObjectRequest objectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                sl,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {


                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Rest Response", error.toString());
                                    }
                                }
                        );
                        requestQueue.add(objectRequest);
                        Context context = view.getContext();
                        context.startActivity(new Intent(context, MainActivity.class));

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                    JsonObjectRequest objectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                           url,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {


                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Rest Response", error.toString());
                                }
                            }
                    );
                    requestQueue.add(objectRequest);
                    Context context = view.getContext();
                    context.startActivity(new Intent(context, MainActivity.class));
                }
            });
        }
```
## Feature Section

### Login/Logout:  

The application has a google sign-in feature which is used to authenticate the user through his google account. The main advantage of this feature is to provide portability to the users, they will be able to access the application from any device. 

### Add Class Schedule:  

The application will redirect the user to a form where he can add his schedule. The form will take name, activity type, time slot and location from the user and store it in the database. For storing the data in the database, a REST API is used.  

### Display Class Schedule:  

The user can view his scheduled classes and activities on the main screen of the application. For fetching the data from the database, a REST API is used. 

### Add User Hobbies:

In addition to the scheduled activities, the application will allow the user to set his preferences for his hobbies. Accordingly, the application will analyze his schedule and suggest his preferred hobbies in free time slots. 
 
### Suggest time slots for user hobbies considering the user schedule:

The crux of the application is to provide suggestions to the user for his favourite activities which he is willing to do in his free time. The application will keep the record of the daily schedule and user preferences. Accordingly, the user will receive suggestions by the application for the activities which he can do in his free time slots. 

### Accept/Reject suggested time slots:  

Once the user preferences are taken, then after the application will suggest their preferred activity in the free time slots. The user will have the advantage to accept or reject the suggested time slot by the application. 
Moreover, if the user accepts the suggested time slot, that particular time slot and the activity will be integrated into his schedule. Also, the rejected suggestions will be deleted. 

### Display distance and route from the current location to the scheduled activity:  

More emphatically, it is important to provide ease to the user. In order to achieve this, the application was integrated with google maps. Sole propose of it is to provide the user's current location and his desired activity or planned schedule's location. In addition, the user will be benefited by the directions from the current location to the scheduled/desired activity. 

### Display duration from the current location to the scheduled activity:

In addition to the distance and route, the user will be able to view the time duration to reach a particular activity location. This will help him to calculate how much time will he get to dedicate for that particular activity. 

## Final Project Status
The Application is in the final state, it has all the features that we planned to implement like get student's schedule, hobbies and accommodate and suggest the time to pursue the hobbies. Going forward we are planning to get the student schedule directly from the Google Services or any other third party application like Dalhousie University, SaintMary's University, etc, and perform weekly analysis of the Students hobbies & classes and present it as graphs/pie charts. Planning to develop a Watch application which can be useful and increase the use of our application. In addition, the performance and suggestions provided by the scheduling algorithm will be enhanced. The precision of allocating time slots for the user's preferred activities will be increased. 

#### Minimum Functionality: 
- Login/Logout - Completed
- Add Class Schedule - Completed
- Display Class Schedule - Completed
- Add User hobbies - Completed
- Suggest time slots for user hobbies considering the user schedule - Completed

#### Expected Functionality:
- Accept/Reject suggested time slots - Completed
- Display distance & route from current location to scheduled activity - Completed
- Display duration from the current location to scheduled activity - Completed

#### Bonus Functionality:
- A weekly analysis of user's hobbies and activities - not implemented
 
## Sources
List we have included in your project sources:
- [Logo images](https://www.freepik.com/)
- [Splash Screen](https://www.youtube.com)
- [Nielson Heuristics](https://www.nngroup.com/articles/ten-usability-heuristics/)
- [Android libraries](https://developer.android.com/)
- [Android references](https://github.com)
- [Schedule Forms](https://www.youtube.com)

#References

[1]"Different morning routines at different times", Freepik. [Online]. Available: https://www.freepik.com/free-vector/different-morning-routines-different-times_1824709.htm. [Accessed: 10- Mar- 2019].

[2]"Volley overview  |  Android Developers", Android Developers. [Online]. Available: https://developer.android.com/training/volley. [Accessed: 10- Mar- 2019].

[3]"Create a List with RecyclerView  |  Android Developers", Android Developers. [Online]. Available: https://developer.android.com/guide/topics/ui/layout/recyclerview. [Accessed: 10- Mar- 2019].

[4]"Adding a Map with a Marker  |  Maps SDK for Android  |  Google Developers", Google Developers. [Online]. Available: https://developers.google.com/maps/documentation/android-sdk/map-with-marker. [Accessed: 10- Mar- 2019].

[5]"Start Integrating Google Sign-In into Your Android App  |  Google Sign-In for Android  |  Google Developers", Google Developers. [Online]. Available: https://developers.google.com/identity/sign-in/android/start-integrating. [Accessed: 10- Mar- 2019].

[6]"google/gson", GitHub. [Online]. Available: https://github.com/google/gson. [Accessed: 10- Mar- 2019].

[7]"google/gson", GitHub. [Online]. Available: https://github.com/google/gson. [Accessed: 11- Mar- 2019].

[8]"How to create registration form in android studio in hindi", YouTube. [Online]. Available: https://www.youtube.com/watch?v=AqOE_fySwE0. [Accessed: 10- Mar- 2019].

[9]"Android Drop Down List Tutorial", YouTube. [Online]. Available: https://www.youtube.com/watch?v=urQp7KsQhW8. [Accessed: 10- Mar- 2019].

[10]S. How to align a TextView, L. Vo and S. Infotech, "How to align a TextView, Spinner and Button in same row in android", Stack Overflow. [Online]. Available: https://stackoverflow.com/questions/22910067/how-to-align-a-textview-spinner-and-button-in-same-row-in-android. [Accessed: 12- Mar- 2019].

[11]H. string?, A. Lai, L. Poptani, D. Vardhan and T. Mogral, "How to get Spinner selected item value to string?", Stack Overflow. [Online]. Available: https://stackoverflow.com/questions/10331854/how-to-get-spinner-selected-item-value-to-string?rq=1. [Accessed: 13- Mar- 2019].

[12]H. numbers?, D. Babak, A. Myller, S. Benner and P. Betos, "How to force EditText to accept only numbers?", Stack Overflow. [Online]. Available: https://stackoverflow.com/questions/9334314/how-to-force-edittext-to-accept-only-numbers. [Accessed: 12- Mar- 2019].

[13]"Android Drop Down List Tutorial", YouTube. [Online]. Available: https://www.youtube.com/watch?v=urQp7KsQhW8. [Accessed: 12- Mar- 2019].

[14]"Android Volley Tutorial – Making HTTP GET, POST, PUT | Alif's Blog", Itsalif.info. [Online]. Available: https://www.itsalif.info/content/android-volley-tutorial-http-get-post-put. [Accessed: 12- Mar- 2019].

[15]"Android Volley Post Request", YouTube. [Online]. Available: https://www.youtube.com/watch?v=M2KKIqrp8Y0. [Accessed: 13- Mar- 2019].

[16]u. NetworkSecurityConfig: No Network Security Config specified, Q. learner and I. Developer, "NetworkSecurityConfig: No Network Security Config specified, using platform default", Stack Overflow. [Online]. Available: https://stackoverflow.com/questions/53984725/networksecurityconfig-no-network-security-config-specified-using-platform-defa. [Accessed: 13- Mar- 2019].

[17]H. header?, p. Kartpay, p. Kartpay and H. Deshmukh, "How to use POST request in android Volley library with params and header?", Stack Overflow. [Online]. Available: https://stackoverflow.com/questions/46171093/how-to-use-post-request-in-android-volley-library-with-params-and-header. [Accessed: 13- Mar- 2019].

[18]"Java String substring() method - javatpoint", www.javatpoint.com. [Online]. Available: https://www.javatpoint.com/java-string-substring. [Accessed: 13- Mar- 2019].

[19]S. Bar, "Styling the Action Bar | Android Developers", Androiddocs.com. [Online]. Available: http://www.androiddocs.com/training/basics/actionbar/styling.html. [Accessed: 13- Mar- 2019].

[20]A. background, S. Jassani and B. Kamenov, "Android gradient background", Stack Overflow. [Online]. Available: https://stackoverflow.com/questions/37996959/android-gradient-background. [Accessed: 13- Mar- 2019].

[21]"Google Maps Intents for Android  |  Maps URLs  |  Google Developers", Google Developers. [Online]. Available: https://developers.google.com/maps/documentation/urls/android-intents. [Accessed: 20- Mar- 2019].

[22]"How to Call REST API in Android", YouTube. [Online]. Available: https://www.youtube.com/watch?v=DpEg_UVkv6E. [Accessed: 20- Mar- 2019].

[23]"Make a standard request  |  Android Developers", Android Developers. [Online]. Available: https://developer.android.com/training/volley/request. [Accessed: 20- Mar- 2019].

[24]"Create a List with RecyclerView  |  Android Developers", Android Developers, 2019. [Online]. Available: https://developer.android.com/guide/topics/ui/layout/recyclerview. [Accessed: 20- Mar- 2019].

[25]R. android et al., "Rounded corner for textview in android", Stack Overflow. [Online]. Available: https://stackoverflow.com/questions/18781902/rounded-corner-for-textview-in-android. [Accessed: 20- Mar- 2019].

[26]"How to create a navigation bar in android studio", YouTube. [Online]. Available: https://www.youtube.com/watch?v=0WC0VU0hw2w. [Accessed: 20- Mar- 2019].

[27]"Integrating Google Sign-In into Your Android App  |  Google Sign-In for Android  |  Google Developers", Google Developers, 2019. [Online]. Available: https://developers.google.com/identity/sign-in/android/sign-in. [Accessed: 20- Mar- 2019].

[28]"Authenticating Your Client  |  Google APIs for Android  |  Google Developers", Google Developers, 2019. [Online]. Available: https://developers.google.com/android/guides/client-auth. [Accessed: 19- Mar- 2019].

[29]A. com.android.support:appcompat-v7.28.0.0 and M. Bhat, "Android Studio Failed to resolve: com.android.support:appcompat-v7.28.0.0", Stack Overflow. [Online]. Available: https://stackoverflow.com/questions/52670419/android-studio-failed-to-resolve-com-android-supportappcompat-v7-28-0-0. [Accessed: 19- Mar- 2019].

[30]"How To Make Splash Screen in Android Studio", YouTube, 2019. [Online]. Available: https://youtu.be/ND6a4V-xdjI. [Accessed: 20- Mar- 2019].

[31]"14,056 Free vector icons of music player", Flaticon. [Online]. Available: https://www.flaticon.com/free-icons/music-player. [Accessed: 25- Mar- 2019].

[32]"OpenStreetMap Nominatim: Search", Nominatim.openstreetmap.org. [Online]. Available: https://nominatim.openstreetmap.org/. [Accessed: 24- Mar- 2019].

[33]"People API Client Library for Java  |  API Client Library for Java  |  Google Developers", Google Developers, 2019. [Online]. Available: https://developers.google.com/api-client-library/java/apis/people/v1. [Accessed: 25- Mar- 2019].