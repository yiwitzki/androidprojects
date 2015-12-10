package com.tp.Calculator.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.tp.Calculator.R;
import com.tp.Calculator.base.BaseActivity;
import com.tp.Calculator.db.NumCompute;

public class CalculatorActivity extends BaseActivity
{
    private Button[] numButton = new Button[11];
    private int[] numButtonId = new int[]{R.id.num0_tv, R.id.num1_tv, R.id.num2_tv, R.id.num3_tv, R.id.num4_tv, R.id.num5_tv, R.id.num6_tv, R.id.num7_tv, R.id.num8_tv, R.id.num9_tv, R.id.dot_tv};
    private Button[] opButton = new Button[6];
    private int[] opButtonId = new int[]{R.id.add_tv, R.id.sub_tv, R.id.multiple_tv, R.id.divide_tv, R.id.equal_tv, R.id.delete_tv};
    private NumCompute nc = new NumCompute();
    private TextView result_tv;
    private final String Tag = "CA tag";
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_layout);
        double lastResult = readPreference();
        result_tv.setText(String.valueOf(lastResult));
    }
    private void wirtePreference(double result)
    {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(getString(R.string.cal_result), (float) result);
        editor.commit();
    }
    private double readPreference()
    {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        double preference = sharedPref.getFloat(getString(R.string.cal_result), 0);
        return preference;
    }
    @Override
    protected void findView()
    {
        for (int i = 0; i < numButton.length; i++)
            numButton[i] = (Button) findViewById(numButtonId[i]);
        for (int i = 0; i < opButton.length; i++)
            opButton[i] = (Button) findViewById(opButtonId[i]);
        result_tv = (TextView) findViewById(R.id.result_tv);
    }

    @Override
    protected void initView()
    {
    }
    private StringBuilder sb = new StringBuilder();
    @Override
    protected void setOnClick()
    {
        for (Button button : numButton)
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Button button = (Button)view;
                    String content = String.valueOf(button.getText());
                    sb.append(content);
                    Log.d(Tag, content);
                    result_tv.setText(sb.toString());
                }
            });
        for (Button button : opButton)
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    double result = 0;
                    Button button = (Button) view;
                    String content = String.valueOf(button.getText());
                    if (content.equals("="))
                    {
                        nc.RecordInput(sb.toString());
                        Log.d(Tag, sb.toString());
                        result = nc.getResult();
                        result_tv.setText(String.valueOf(result));
                        sb.delete(0, sb.length());
                        sb.append(result);
                        wirtePreference(result);
                    }
                    else if (content.equals("delete"))
                    {
                        if (sb.length() > 0)
                        {
                            sb.deleteCharAt(sb.length() - 1);
                            if (sb.length() == 0)
                                result_tv.setText("0");
                            else
                                result_tv.setText(sb.toString());
                        }
                    }
                    else
                    {
                        sb.append(content);
                        result_tv.setText(sb.toString());
                    }
                }
            });
    }
}
