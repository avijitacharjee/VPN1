// Generated code from Butter Knife. Do not modify!
package com.abdev.codestervpn.activity;

import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.abdev.codestervpn.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChooseServerActivity_ViewBinding implements Unbinder {
  private ChooseServerActivity target;

  @UiThread
  public ChooseServerActivity_ViewBinding(ChooseServerActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ChooseServerActivity_ViewBinding(ChooseServerActivity target, View source) {
    this.target = target;

    target.regionsRecyclerView = Utils.findRequiredViewAsType(source, R.id.regions_recycler_view, "field 'regionsRecyclerView'", RecyclerView.class);
    target.regionsProgressBar = Utils.findRequiredViewAsType(source, R.id.regions_progress, "field 'regionsProgressBar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ChooseServerActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.regionsRecyclerView = null;
    target.regionsProgressBar = null;
  }
}
