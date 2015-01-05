package csg.HelloAndroid;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment_1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment_1 extends Fragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;
    private MediaPlayer mPlayer;
    private float lastReading[];
    private int lastReadingIdx;
    private int buzzThreshold;
    private int initDone;
    private MySensor pS[];
    private SensorManager snsMgr;
    private float sensorVals[][];
    private int samplingDelay;

    private Timer timer;
    private MyTimerTask timerTask;

    private OnFragmentInteractionListener mListener;
    private String logFile;
    FileOutputStream fOut;
    private FileOutputStream outputStream;

    private class MyTimerTask extends TimerTask{

        @Override
        public void run()
        {
            if(samplingDelay != SensorManager.SENSOR_DELAY_GAME)
            {
                int idx = 0;
                if(pS != null) {
                    for (MySensor mySensor : pS) {
                        if (mySensor.enable == true) {
                            writeToFile(idx);
                        }
                        idx++;
                    }
                }
            }
        }
    }

    private class MySensor {
        public Sensor sensor;
        public boolean enable;
        public String name;
        public int viewId;

        public void mySensor() {
            sensor = null;
            enable = false;
            name = "";
        }
    }

    private class myAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            while(1==1)
            {
                SystemClock.sleep(1000);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private myAsyncTask bgTsk;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    static final float ALPHA = 1;
    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    public void writeToFile(int idx)
    {
        String output = pS[idx].name ;
        for (float v : sensorVals[idx]) {
            output += ", " + v;
        }
        output += "\n";

        output = Long.toString(System.currentTimeMillis()) + ", " + output;
        if(outputStream != null)
        {
            try {
                outputStream.write(output.toString().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int lpArray = getResources().getInteger(R.integer.lpArray);
        float[] values = event.values;
        if(initDone == 0)
        {
            initDone = 1;
            addListenerOnButton();
        }
        int idx = 0;
        for (MySensor sensor : pS) {
            if(pS[idx].sensor == event.sensor) break;
            idx++;
        }

        sensorVals[idx] = lowPass(values.clone(), sensorVals[idx]);
        TextView tV = (TextView) getActivity().findViewById(pS[idx].viewId);
        tV.setText(pS[idx].name + ", " + values[0] + ", " + values[1] + ", " + values[2] + "\n");

        if(samplingDelay == SensorManager.SENSOR_DELAY_GAME)
            writeToFile(idx);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment_1.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment_1 newInstance(String param1, String param2) {
        BlankFragment_1 fragment = new BlankFragment_1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BlankFragment_1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            mContext = getActivity();
            mPlayer = MediaPlayer.create(mContext, R.raw.buzz);
            buzzThreshold = 1;
            lastReadingIdx = 0;
            int i, lpArray = mContext.getResources().getInteger(R.integer.lpArray);
            lastReading = new float[lpArray];
            for(i=0; i<lpArray; i++)
            {
                lastReading[i] = (float)-1;
            }
        }
        snsMgr = (SensorManager) mContext.getSystemService(Service.SENSOR_SERVICE);
        pS = new MySensor[snsMgr.getSensorList(Sensor.TYPE_ALL).size()];
        List<Sensor> sensorList = snsMgr.getSensorList(Sensor.TYPE_ALL);
        sensorVals = new float[snsMgr.getSensorList(Sensor.TYPE_ALL).size()][];
        int idx = 0;
        for (Sensor sensor : sensorList) {
            pS[idx] = new MySensor();
            pS[idx].sensor = sensor;
            switch (sensor.getType())
            {
                case Sensor.TYPE_ACCELEROMETER:
                    pS[idx].enable = true;
                    pS[idx].name = "ACCL";
                    pS[idx].viewId = R.id.acclTextView;
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    pS[idx].enable = true;
                    pS[idx].name = "GYRO";
                    pS[idx].viewId = R.id.gyroTextView;
                    break;
                default:
                    pS[idx].enable = false;
                    pS[idx].name = "NULL";
                    break;
            }

            if(pS[idx].enable) {
                snsMgr.registerListener(this, pS[idx].sensor, SensorManager.SENSOR_DELAY_GAME);
            }
            idx++;
        }
        initDone = 0;
        samplingDelay = SensorManager.SENSOR_DELAY_GAME;
        bgTsk = new myAsyncTask();
        bgTsk.execute();

        timerTask = new MyTimerTask();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_fragment_1, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction_BlankFragment_1();
        }
    }

    public void addListenerOnButton() {
        Button button = (Button) getActivity().findViewById(R.id.my_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                //intent.setType("file/*.dat");
                //startActivityForResult(intent, 1);

                try {
                    Button button = (Button) arg0;
                    if(outputStream != null) {
                        outputStream.close();
                        outputStream = null;
                        button.setText(R.string.StartRecording);
                        if(timer != null){
                            timer.cancel();
                        }
                    }
                    else {
                        String text;
                        TextView tv = (TextView) getActivity().findViewById(R.id.sampleRate);
                        text = tv.getText().toString();
                        if(text.equals(""))
                        {
                            samplingDelay = SensorManager.SENSOR_DELAY_GAME;
                        }
                        else
                        {
                            samplingDelay = Integer.parseInt(text);
                            timer = new Timer();
                            timerTask = new MyTimerTask();
                            timer.schedule(timerTask, 100, samplingDelay);
                        }
                        logFile = Long.toString(System.currentTimeMillis()) + ".txt";
                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), logFile);
                        file.createNewFile();
                        outputStream = new FileOutputStream(file, true);
                        button.setText(R.string.StopRecording);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri resData = data.getData();
            String FilePath = data.getData().getLastPathSegment();
            logFile = FilePath;
            TextView tv = (TextView) getActivity().findViewById(R.id.gyroTextView);
            tv.setText(FilePath);

            FileOutputStream outputStream;

            try {
                logFile = Long.toString(System.currentTimeMillis()) + ".txt";
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), logFile);
                file.createNewFile();
                outputStream = new FileOutputStream(file, true);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        snsMgr.unregisterListener(this);
        pS = null;
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction_BlankFragment_1();
    }

}
