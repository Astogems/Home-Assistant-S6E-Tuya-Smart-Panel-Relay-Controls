package com.smatek.tytest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import androidx.fragment.app.FragmentManager;
import com.MyApplication;
import com.smatek.tytest.utils.ChannelUtils;

public class RelayTest extends BaseFragment {
    private static final String TAG = "RelayTest";
    /* access modifiers changed from: private */
    public static final Object relayclock1 = new Object();
    /* access modifiers changed from: private */
    public static final Object relayclock2 = new Object();
    Boolean Relay1Checked;
    Boolean Relay2Checked;
    /* access modifiers changed from: private */
    public Button btnRelay1;
    /* access modifiers changed from: private */
    public Button btnRelay2;
    String gpioRelay1 = "gpio114";
    String gpioRelay2 = "gpio115";
    Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            RelayTest.this.pass.setEnabled(true);
        }
    };
    int relay1;
    int relay2;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if ("dnake7_84in".equals(ChannelUtils.getChannelName(MyApplication.getContextObject()))) {
            View inflate = layoutInflater.inflate(R.layout.fragment_relay_can_test, viewGroup, false);
            ((TextView) inflate.findViewById(R.id.tv_relay)).setText("观察两路灯光是否在变化");
            this.pass = (Button) inflate.findViewById(R.id.pass);
            this.pass.setEnabled(false);
            TestActivity.execRootCmd("killall test_bin");
            TestActivity.execRootCmd("/dnake/bin/test_bin 2 2 1 &");
            this.pass.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.d(RelayTest.TAG, "onClick: pass click");
                    PrefUtils.setKeyRelay(true);
                    MYFragmentManager.getInstance((FragmentManager) null).nextTest();
                }
            });
            this.pass.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    PrefUtils.setKeyRelay(true);
                    MYFragmentManager.getInstance((FragmentManager) null).lastTest();
                    return false;
                }
            });
            this.ng = (Button) inflate.findViewById(R.id.ng);
            this.ng.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.d(RelayTest.TAG, "onClick: ng click");
                    PrefUtils.setKeyRelay(false);
                    MYFragmentManager.getInstance((FragmentManager) null).nextTest();
                }
            });
            this.back = (Button) inflate.findViewById(R.id.back);
            this.back.getBackground().setAlpha(100);
            this.back.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.d(RelayTest.TAG, "onClick: back click");
                    MYFragmentManager.getInstance((FragmentManager) null).prevTest();
                }
            });
            this.mHandler.sendEmptyMessageDelayed(0, 3000);
            return inflate;
        }
        View inflate2 = layoutInflater.inflate(R.layout.fragment_relay_test, viewGroup, false);
        if ("dnake6in".equals(ChannelUtils.getChannelName(MyApplication.getContextObject()))) {
            this.gpioRelay1 = "gpio107";
            this.gpioRelay2 = "gpio108";
        } else {
            this.gpioRelay1 = "gpio114";
            this.gpioRelay2 = "gpio115";
        }
        this.pass = (Button) inflate2.findViewById(R.id.pass);
        this.btnRelay1 = (Button) inflate2.findViewById(R.id.btnRelay1);
        this.btnRelay1.setTextColor(-16776961);
        this.btnRelay2 = (Button) inflate2.findViewById(R.id.btnRelay2);
        this.btnRelay2.setTextColor(-16776961);
        BaseFragment.execRootCmd("echo 0 > /sys/class/gpio/" + this.gpioRelay2 + "/value");
        BaseFragment.execRootCmd("echo 0 > /sys/class/gpio/" + this.gpioRelay1 + "/value");
        this.Relay1Checked = false;
        this.Relay2Checked = false;
        this.pass.setEnabled(false);
        this.btnRelay1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                synchronized (RelayTest.relayclock1) {
                    if (!RelayTest.this.Relay1Checked.booleanValue()) {
                        BaseFragment.execRootCmd("echo 1 > /sys/class/gpio/" + RelayTest.this.gpioRelay1 + "/value");
                        RelayTest.this.btnRelay1.setTextColor(SupportMenu.CATEGORY_MASK);
                        RelayTest.this.Relay1Checked = true;
                    } else {
                        RelayTest.this.Relay1Checked = false;
                        BaseFragment.execRootCmd("echo 0 > /sys/class/gpio/" + RelayTest.this.gpioRelay1 + "/value");
                        RelayTest.this.btnRelay1.setTextColor(-16776961);
                    }
                    RelayTest.this.relay1++;
                    if (RelayTest.this.relay1 >= 2 && RelayTest.this.relay2 >= 2) {
                        RelayTest.this.pass.setEnabled(true);
                    }
                }
            }
        });
        this.btnRelay2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                synchronized (RelayTest.relayclock2) {
                    if (!RelayTest.this.Relay2Checked.booleanValue()) {
                        RelayTest.this.Relay2Checked = true;
                        RelayTest.this.btnRelay2.setTextColor(SupportMenu.CATEGORY_MASK);
                        BaseFragment.execRootCmd("echo 1 > /sys/class/gpio/" + RelayTest.this.gpioRelay2 + "/value");
                    } else {
                        BaseFragment.execRootCmd("echo 0 > /sys/class/gpio/" + RelayTest.this.gpioRelay2 + "/value");
                        RelayTest.this.Relay2Checked = false;
                        RelayTest.this.btnRelay2.setTextColor(-16776961);
                    }
                    RelayTest.this.relay2++;
                    if (RelayTest.this.relay1 >= 2 && RelayTest.this.relay2 >= 2) {
                        RelayTest.this.pass.setEnabled(true);
                    }
                }
            }
        });
        this.pass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(RelayTest.TAG, "onClick: pass click");
                PrefUtils.setKeyRelay(true);
                MYFragmentManager.getInstance((FragmentManager) null).nextTest();
            }
        });
        this.pass.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                PrefUtils.setKeyRelay(true);
                MYFragmentManager.getInstance((FragmentManager) null).lastTest();
                return false;
            }
        });
        this.ng = (Button) inflate2.findViewById(R.id.ng);
        this.ng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(RelayTest.TAG, "onClick: ng click");
                PrefUtils.setKeyRelay(false);
                MYFragmentManager.getInstance((FragmentManager) null).nextTest();
            }
        });
        this.back = (Button) inflate2.findViewById(R.id.back);
        this.back.getBackground().setAlpha(100);
        this.back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(RelayTest.TAG, "onClick: back click");
                MYFragmentManager.getInstance((FragmentManager) null).prevTest();
            }
        });
        return inflate2;
    }

    public void onDetach() {
        super.onDetach();
        BaseFragment.execRootCmd("echo 0 > /sys/class/gpio/" + this.gpioRelay2 + "/value");
        BaseFragment.execRootCmd("echo 0 > /sys/class/gpio/" + this.gpioRelay1 + "/value");
    }
}
