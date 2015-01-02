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
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


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
    private static int initDone = 0;
    private Sensor pS;
    private SensorManager snsMgr;
    private float lxSensorVal[];

    private OnFragmentInteractionListener mListener;

    private class myAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            while(1==1)
            {
                SystemClock.sleep(100);
                lxSensorVal = lowPass(lastReading.clone(), lxSensorVal);
                float chkVal = lxSensorVal[0];
                if(chkVal < buzzThreshold)
                {
                    mPlayer.start();
                }
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

    static final float ALPHA = 0.25f;
    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int lpArray = getResources().getInteger(R.integer.lpArray);
        float[] values = event.values;
        TextView tV = (TextView) getActivity().findViewById(R.id.lxTextView);
        if(initDone == 0)
        {
            initDone = 1;
            addListenerOnButton();
        }
        lastReading = values.clone();
        lxSensorVal = lowPass(values.clone(), lxSensorVal);
        float chkVal = lxSensorVal[0];
        tV.setText("" + chkVal);
        if(chkVal < buzzThreshold)
        {
            mPlayer.start();
        }
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
        pS = snsMgr.getDefaultSensor(Sensor.TYPE_LIGHT);
        snsMgr.registerListener(this, pS, SensorManager.SENSOR_DELAY_UI);
        bgTsk = new myAsyncTask();
        bgTsk.execute();
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
                EditText eT = (EditText) getActivity().findViewById(R.id.edit_message);
                if (eT.getText().toString().contentEquals("")) {
                    buzzThreshold = 100;
                }
                else
                {
                    buzzThreshold = Integer.parseInt(eT.getText().toString());
                }
            }

        });

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
