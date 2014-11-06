Modify Before Run:
1. Manifest - 

Add permission -

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />


uses-feature-

    <uses-feature
        android:glEsVersion="0x00020000"
        android:required="true" />
    <uses-feature
        android:glEsVersion="0x00020000"
        android:required="true" />


meta-data-

        <meta-data
            android:name="com.google.android.maps.v2.API_KEY"
            android:value="AIzaSyAHrR9PdeqTGD_JlMFTo05B-2ABWlU0Bqc" />
        <meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />