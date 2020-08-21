// Generated code from Butter Knife. Do not modify!
package com.abdev.codestervpn.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.abdev.codestervpn.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UIActivity_ViewBinding implements Unbinder {
  private UIActivity target;

  private View view7f0a009b;

  private View view7f0a00d7;

  private View view7f0a0092;

  @UiThread
  public UIActivity_ViewBinding(UIActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UIActivity_ViewBinding(final UIActivity target, View source) {
    this.target = target;

    View view;
    target.server_ip = Utils.findRequiredViewAsType(source, R.id.server_ip, "field 'server_ip'", TextView.class);
    view = Utils.findRequiredView(source, R.id.img_connect, "field 'img_connect' and method 'onConnectBtnClick'");
    target.img_connect = Utils.castView(view, R.id.img_connect, "field 'img_connect'", ImageView.class);
    view7f0a009b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onConnectBtnClick(p0);
      }
    });
    target.connectionStateTextView = Utils.findRequiredViewAsType(source, R.id.connection_state, "field 'connectionStateTextView'", TextView.class);
    target.connectionProgressBar = Utils.findRequiredViewAsType(source, R.id.connection_progress, "field 'connectionProgressBar'", ProgressBar.class);
    target.trafficLimitTextView = Utils.findRequiredViewAsType(source, R.id.traffic_limit, "field 'trafficLimitTextView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.optimal_server_btn, "field 'currentServerBtn' and method 'onServerChooserClick'");
    target.currentServerBtn = Utils.castView(view, R.id.optimal_server_btn, "field 'currentServerBtn'", LinearLayout.class);
    view7f0a00d7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onServerChooserClick(p0);
      }
    });
    target.selectedServerTextView = Utils.findRequiredViewAsType(source, R.id.selected_server, "field 'selectedServerTextView'", TextView.class);
    target.country_flag = Utils.findRequiredViewAsType(source, R.id.country_flag, "field 'country_flag'", ImageView.class);
    target.txt_download = Utils.findRequiredViewAsType(source, R.id.txt_download, "field 'txt_download'", TextView.class);
    target.txt_upload = Utils.findRequiredViewAsType(source, R.id.txt_upload, "field 'txt_upload'", TextView.class);
    target.lin_spped = Utils.findRequiredViewAsType(source, R.id.lin_spped, "field 'lin_spped'", RelativeLayout.class);
    target.lay_pro = Utils.findRequiredViewAsType(source, R.id.lay_pro, "field 'lay_pro'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.hamburger_btn, "field 'hamburger_btn' and method 'onhamburgerclick'");
    target.hamburger_btn = Utils.castView(view, R.id.hamburger_btn, "field 'hamburger_btn'", LinearLayout.class);
    view7f0a0092 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onhamburgerclick(p0);
      }
    });
    target.main_layout = Utils.findRequiredViewAsType(source, R.id.main_layout, "field 'main_layout'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UIActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.server_ip = null;
    target.img_connect = null;
    target.connectionStateTextView = null;
    target.connectionProgressBar = null;
    target.trafficLimitTextView = null;
    target.currentServerBtn = null;
    target.selectedServerTextView = null;
    target.country_flag = null;
    target.txt_download = null;
    target.txt_upload = null;
    target.lin_spped = null;
    target.lay_pro = null;
    target.hamburger_btn = null;
    target.main_layout = null;

    view7f0a009b.setOnClickListener(null);
    view7f0a009b = null;
    view7f0a00d7.setOnClickListener(null);
    view7f0a00d7 = null;
    view7f0a0092.setOnClickListener(null);
    view7f0a0092 = null;
  }
}
