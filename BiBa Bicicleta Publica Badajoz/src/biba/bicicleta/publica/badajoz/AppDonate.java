package biba.bicicleta.publica.badajoz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class AppDonate {

	// Change for your needs!

	private final static String APP_PACKAGE_NAME = "biba.bicicleta.publica.badajoz.donate";

	private final static int DAYS_UNTIL_PROMPT = 5;
	private final static int LAUNCH_UNTIL_PROMPT = 10;

	public static void app_launched(Context mContext) {
		SharedPreferences prefs = mContext
				.getSharedPreferences("donate_app", 0);
		if (prefs.getBoolean("dontshowagain", false)) {
			return;
		}

		SharedPreferences.Editor editor = prefs.edit();

		// Add to launch Counter
		long launch_count = prefs.getLong("launch_count", 0) + 1;
		editor.putLong("launch_count", launch_count);

		// Get Date of first launch
		Long date_firstLaunch = prefs.getLong("date_first_launch", 0);
		if (date_firstLaunch == 0) {
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong("date_first_launch", date_firstLaunch);
		}

		// Wait at least X days to launch
		if (launch_count >= LAUNCH_UNTIL_PROMPT) {

			if (System.currentTimeMillis() >= date_firstLaunch
					+ (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
				showDonateDialog(mContext, editor);
			}

		}

		editor.commit();

	}

	public static void openDonateVersion(Context mContext){
		mContext.startActivity(new Intent(
				Intent.ACTION_VIEW, Uri
						.parse("market://details?id="
								+ APP_PACKAGE_NAME)));
	}
	
	public static void showDonateDialog(final Context mContext,
			final SharedPreferences.Editor editor) {
		Dialog dialog = new Dialog(mContext);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		String message = mContext.getString(R.string.DonateDialog);
		builder.setMessage(message)
				.setTitle(mContext.getString(R.string.DonateTitle))
				.setIcon(mContext.getApplicationInfo().icon)
				.setCancelable(false)
				.setPositiveButton(mContext.getString(R.string.DonateNow),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								editor.putBoolean("dontshowagain", true);
								editor.commit();
								openDonateVersion(mContext);
								dialog.dismiss();
							}
						})
				.setNeutralButton(mContext.getString(R.string.Later),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								editor.putLong("launch_count", 5);
								dialog.dismiss();

							}
						})
				.setNegativeButton(mContext.getString(R.string.NoThanks),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (editor != null) {
									editor.putBoolean("dontshowagain", true);
									editor.commit();
								}
								dialog.dismiss();

							}
						});
		dialog = builder.create();

		dialog.show();
	}
	

}
