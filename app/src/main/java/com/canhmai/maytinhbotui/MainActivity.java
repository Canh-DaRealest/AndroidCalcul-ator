package com.canhmai.maytinhbotui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.canhmai.maytinhbotui.databinding.ActivityMainBinding;

import java.text.DecimalFormat;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    private static final String MULTI = "×";
    private static final String DIV = "÷";
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String PERCENT = "%";
    private String EQUALS = "=";
    private ActivityMainBinding binding;
    private String value = null;
    private boolean isEqualClicked = false;
    private double firstNum = 0;
    private double secondNum = 0;
    private MediaPlayer mediaPlayer;
    private DecimalFormat decimalFormat;
    private String previousState = null;
    private boolean isCalculating = false;
    private String pattern = "###,###,###,###.#####";
    private String history, result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intiView();
        decimalFormat = new DecimalFormat(pattern);
        decimalFormat = new DecimalFormat();
    }

    private void intiView() {
        binding.tvInput.requestFocus();
        setClick();
    }

    private void setClick() {

        binding.btPercent.setOnClickListener(this::doClickPercent);

        binding.bt7.setOnClickListener(v -> updateText("7"));
        binding.bt8.setOnClickListener(v -> updateText("8"));
        binding.bt9.setOnClickListener(v -> updateText("9"));

        binding.bt4.setOnClickListener(v -> updateText("4"));
        binding.bt5.setOnClickListener(v -> updateText("5"));
        binding.bt6.setOnClickListener(v -> updateText("6"));

        binding.bt1.setOnClickListener(v -> updateText("1"));
        binding.bt2.setOnClickListener(v -> updateText("2"));
        binding.bt3.setOnClickListener(v -> updateText("3"));

        binding.btNegative.setOnClickListener(this::doClickNegative);
        binding.bt0.setOnClickListener(v -> updateText("0"));
        binding.btDot.setOnClickListener(this::doClickDot);

        binding.del.setOnClickListener(this::doClickDel);

        setTagView();


        binding.btDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isEqualClicked) {
                    history = binding.tvInput.getText().toString();
                    result = binding.tvOutput.getText().toString();
                    binding.tvInput.setText((history + "/"));
                } else {
                    history = binding.tvInput.getText().toString();
                    result = binding.tvOutput.getText().toString();
                    binding.tvInput.setText((history + result + "/"));
                }

                if (isCalculating) {

                    if (previousState == MULTI) {
                        multi();
                    } else {
                        if (previousState == PLUS) {

                            plus();
                        } else {

                            if (previousState == MINUS) {

                                minus();
                            } else {

                                div();


                            }
                        }
                    }
                }
                value = null;
                previousState = DIV;
                binding.tvOperator.setText(DIV);
                isCalculating = false;
                isEqualClicked = false;
            }

        });

        binding.btPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound();

                if (isEqualClicked) {
                    history = binding.tvInput.getText().toString();
                    result = binding.tvOutput.getText().toString();
                    binding.tvInput.setText((history + "+"));
                } else {
                    history = binding.tvInput.getText().toString();
                    result = binding.tvOutput.getText().toString();
                    binding.tvInput.setText((history + result + "+"));
                }

                if (isCalculating) {

                    if (previousState == MULTI) {
                        multi();
                    } else {
                        if (previousState == DIV) {

                            div();
                        } else {

                            if (previousState == MINUS) {

                                minus();
                            } else {

                                plus();


                            }
                        }
                    }
                }
                isEqualClicked = false;
                value = null;
                previousState = PLUS;
                binding.tvOperator.setText(PLUS);
                isCalculating = false;
            }
        });

        binding.btMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playClickSound();
                if (isEqualClicked) {
                    history = binding.tvInput.getText().toString();
                    result = binding.tvOutput.getText().toString();
                    binding.tvInput.setText((history + "*"));
                } else {
                    history = binding.tvInput.getText().toString();
                    result = binding.tvOutput.getText().toString();
                    binding.tvInput.setText((history + result + "*"));
                }

                if (isCalculating) {

                    if (previousState == PLUS) {
                        plus();
                    } else {
                        if (previousState == DIV) {

                            div();
                        } else {

                            if (previousState == MINUS) {

                                minus();
                            } else {


                                multi();


                            }
                        }
                    }
                }

                value = null;
                previousState = MULTI;
                binding.tvOperator.setText(MULTI);
                isCalculating = false;
                isEqualClicked = false;
            }

        });


        binding.btMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playClickSound();
                if (isEqualClicked) {
                    history = binding.tvInput.getText().toString();
                    result = binding.tvOutput.getText().toString();
                    binding.tvInput.setText((history + "-"));
                } else {
                    history = binding.tvInput.getText().toString();
                    result = binding.tvOutput.getText().toString();
                    binding.tvInput.setText((history + result + "-"));
                }

                if (isCalculating) {

                    if (previousState == MULTI) {
                        multi();
                    } else {
                        if (previousState == DIV) {

                            div();
                        } else {

                            if (previousState == PLUS) {

                                plus();
                            } else {

                                minus();

                            }
                        }
                    }
                }

                value = null;
                previousState = MINUS;
                isCalculating = false;
                isEqualClicked = false;
                binding.tvOperator.setText(MINUS);
            }

        });

        binding.btEquals.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                playClickSound();
                if (isEqualClicked) {
                    return;
                }

                history = binding.tvInput.getText().toString();
                result = binding.tvOutput.getText().toString();

                binding.tvInput.setText((history + result));

                if (isCalculating) {

                    if (previousState == MULTI) {
                        multi();
                    } else {
                        if (previousState == DIV) {

                            div();
                        } else {

                            if (previousState == PLUS) {

                                plus();
                            } else {
                                if (previousState == MINUS) {

                                    minus();
                                } else {

                                    firstNum = Double.parseDouble(binding.tvOutput.getText().toString());

                                }
                            }
                        }
                    }


                }
                isCalculating = false;
                isEqualClicked = true;
                binding.tvOperator.setText(EQUALS);
            }
        });

        binding.btAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound();
                doClickAC();
            }
        });
    }

    private void doClickAC() {
        value = null;
        isCalculating = false;
        binding.tvInput.setText("");
        binding.tvOutput.setText("0");
        binding.tvOperator.setText("");
        previousState = null;
        firstNum = 0;
        secondNum = 0;
        isEqualClicked = false;
    }


    private void setTagView() {
        binding.btAC.setTag("AC");
        binding.btPercent.setTag("%");
        binding.btDiv.setTag("/");
        binding.bt7.setTag("7");
        binding.bt8.setTag("8");
        binding.bt9.setTag("9");
        binding.btMulti.setTag("*");
        binding.bt4.setTag("4");
        binding.bt5.setTag("5");
        binding.bt6.setTag("6");
        binding.btMinus.setTag("-");
        binding.bt1.setTag("1");
        binding.bt2.setTag("2");
        binding.bt3.setTag("3");
        binding.btPlus.setTag("+");
        binding.btNegative.setTag("(-");
        binding.bt0.setTag("0");
        binding.btDot.setTag(".");
        binding.btEquals.setTag("=");

    }


    private void updateText(String text) {

        playClickSound();
        if (value == null) {
            if (Objects.equals(text, "0")) {
                binding.tvOutput.setText("0");

            } else {
                value = text;
                binding.tvOutput.setText(value);
            }


        } else {
            String val = value.replace("%", "");
            val = val.replace(".", "");

            if (val.length() == 15) {
                Toast.makeText(this, "Too long!!!", Toast.LENGTH_SHORT).show();
                return;
            } else if (Objects.equals(value, "0")) {
                binding.tvOutput.setText("0");

            } else {

                value += text;

                binding.tvOutput.setText(value);
            }

        }


        isCalculating = true;
    }

    private void minus() {


        if (firstNum == 0) {
            firstNum = Double.parseDouble(binding.tvOutput.getText().toString());
        } else {
            secondNum = Double.parseDouble(binding.tvOutput.getText().toString());
            firstNum = firstNum - secondNum;
        }
        binding.tvOutput.setText(decimalFormat.format(firstNum));

        binding.tvOperator.setText(MINUS);

    }

    private void plus() {
        secondNum = Double.parseDouble(binding.tvOutput.getText().toString());
        firstNum = firstNum + secondNum;
        binding.tvOutput.setText(decimalFormat.format(firstNum));
        binding.tvOperator.setText(PLUS);
    }


    private void div() {
        if (firstNum == 0) {
            secondNum = Double.parseDouble(binding.tvOutput.getText().toString());
            firstNum = secondNum;
        } else {
            secondNum = Double.parseDouble(binding.tvOutput.getText().toString());
            firstNum = firstNum / secondNum;


        }
        binding.tvOutput.setText(decimalFormat.format(firstNum));
        binding.tvOperator.setText(DIV);
    }


    private void multi() {
        if (firstNum == 0) {
            firstNum = Double.parseDouble(binding.tvOutput.getText().toString());
        } else {
            secondNum = Double.parseDouble(binding.tvOutput.getText().toString());
            firstNum = firstNum * secondNum;
        }
        binding.tvOutput.setText(decimalFormat.format(firstNum));
        binding.tvOperator.setText(MULTI);

    }


    private void doClickDel(View v) {
        playClickSound();
        String val = binding.tvOutput.getText().toString();

        if (val.length() == 1) {
            doClickAC();
            return;
        }

        if (val.length() > 1) {
            val = val.substring(0, val.length() - 1);

            binding.tvOutput.setText(val);

        }
    }


    private void doClickDot(View v) {

        playClickSound();
        if (value == null) {
            value = "0.";

        } else {
            if (value.contains(".") || value.contains("%")) {
                return;

            }
            value = value + ".";
            binding.tvOutput.setText(value);
        }
    }


    private void doClickNegative(View v) {
        playClickSound();
        String value = binding.tvOutput.getText().toString();


        String leftStr = "";
        String righStr = "";
        String gio = "";

        if (value.equals("0")) {

            value = "-0";

        } else if (value.equals("-0")) {
            value = "0";
        } else {
            if (value.contains("-")) {
                value = value.replace("-", "");

            } else {
                value = "-" + value;
            }
        }
        binding.tvOutput.setText(value);
    }


    private boolean isOperator(char key) {
        return (key + "").equals("×") || (key + "").equals("÷") || (key + "").equals("-") || (key + "").equals("+");
    }

    private void doClickPercent(View v) {
        playClickSound();
        if (value == null) {

            value = "0%";
        } else {
            if (value.contains("%") || value.charAt(value.length() - 1) == '%' || isOperator(value.charAt(value.length() - 1))) {
                return;
            } else {
                value += "%";
                binding.tvOutput.setText(value);

            }
        }
        firstNum = Double.parseDouble(value.replace("%", ""));
        firstNum = firstNum / 100;
        binding.tvInput.setText("(" + value + ")");
        binding.tvOutput.setText(decimalFormat.format(firstNum));

        binding.tvOperator.setText(EQUALS);

        previousState = PERCENT;
        isCalculating = true;
        isEqualClicked = true;
    }

    private void playClickSound() {

        mediaPlayer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(this, R.raw.click);


    }
}
