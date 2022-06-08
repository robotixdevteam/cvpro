package com.example.python_compiler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {

    TextView output;
    EditText CodeArea;
    Button Run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);
        CodeArea = (EditText) findViewById(R.id.codearea);
        Run = (Button) findViewById(R.id.run);

        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));

        Run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Python py = Python.getInstance();
                PyObject pyobj = py.getModule("myscript"); // here we will give name of our python file
                
                PyObject obj = pyobj.callAttr("main",CodeArea.getText().toString());

                output.setText(obj.toString());
            }
        });


    }
}