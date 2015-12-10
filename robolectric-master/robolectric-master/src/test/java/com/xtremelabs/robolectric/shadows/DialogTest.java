package com.xtremelabs.robolectric.shadows;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.WithTestDefaultsRunner;
import com.xtremelabs.robolectric.util.Transcript;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(WithTestDefaultsRunner.class)
public class DialogTest {
	@Test
    public void shouldCallOnDismissListener() throws Exception {
        final Transcript transcript = new Transcript();

        final Dialog dialog = new Dialog(null);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInListener) {
                assertThat((Dialog) dialogInListener, sameInstance(dialog));
                transcript.add("onDismiss called!");
            }
        });

        dialog.dismiss();

        transcript.assertEventsSoFar("onDismiss called!");
    }

    @Test
    public void shouldGetLayoutInflater() {
        Dialog dialog = new Dialog(Robolectric.application);
        assertNotNull(dialog.getLayoutInflater());
    }

    @Test
    public void shouldCallOnStartFromShow() {
        TestDialog dialog = new TestDialog();
        dialog.show();

        assertTrue(dialog.onStartCalled);
    }

    @Test
    public void shouldSetCancelable() {
        Dialog dialog = new Dialog(null);
        ShadowDialog shadow = Robolectric.shadowOf(dialog);

        dialog.setCancelable(false);
        assertThat(shadow.isCancelable(), equalTo(false));
    }

    @Test
    public void shouldDismissTheRealDialogWhenCancelled() throws Exception {
        TestDialog dialog = new TestDialog();
        dialog.cancel();
        assertThat(dialog.wasDismissed, equalTo(true));
    }

    @Test
    public void shouldDefaultCancelableToTrueAsTheSDKDoes() throws Exception {
        Dialog dialog = new Dialog(null);
        ShadowDialog shadow = Robolectric.shadowOf(dialog);

        assertThat(shadow.isCancelable(), equalTo(true));
    }

    @Test
    public void shouldOnlyCallOnCreateOnce() {
        final Transcript transcript = new Transcript();

        Dialog dialog = new Dialog(Robolectric.application) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                transcript.add("onCreate called");
            }
        };

        dialog.show();
        transcript.assertEventsSoFar("onCreate called");

        dialog.dismiss();
        dialog.show();
        transcript.assertNoEventsSoFar();
    }

    @Test
    public void show_setsLatestDialog() {
        Dialog dialog = new Dialog(Robolectric.application);
        assertNull(ShadowDialog.getLatestDialog());
        
        dialog.show();

        assertEquals(dialog, ShadowDialog.getLatestDialog());
        assertNull(ShadowAlertDialog.getLatestAlertDialog());
    }

    @Test
    public void getLatestDialog_shouldReturnARealDialog() throws Exception {
        assertThat(ShadowDialog.getLatestDialog(), nullValue());

        Dialog dialog = new Dialog(null);
        dialog.show();
        assertThat(ShadowDialog.getLatestDialog(), sameInstance(dialog));
    }

    private static class TestDialog extends Dialog {
        boolean onStartCalled = false;
        boolean wasDismissed =  false;

        public TestDialog() {
            super(null);
        }

        @Override
        protected void onStart() {
            onStartCalled = true;
        }

        @Override public void dismiss() {
            super.dismiss();
            wasDismissed = true;
        }
    }

}
