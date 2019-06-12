package xiaolong.cordova.AppUpgrade;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xiaolong.tour.mobileService.R;

/**
 * This class echoes a string called from JavaScript.
 */
public class AppUpgrade extends CordovaPlugin {
	private CallbackContext callbackContext;
	private final String TAG = "AppUpgrade";

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		// your init code here
		Beta.autoCheckUpgrade = false;
		Beta.largeIconId = R.mipmap.ic_launcher;
		Beta.smallIconId = R.mipmap.ic_launcher;
		Beta.defaultBannerId = R.mipmap.ic_launcher;
		Beta.upgradeStateListener = new UpgradeStateListener() {
			@Override
			public void onUpgradeSuccess(boolean isManual) {
				if (callbackContext != null) {
					UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
					if (upgradeInfo != null) {
						try {
							JSONObject result = new JSONObject();
							result.put("title", upgradeInfo.title);
							result.put("newFeature", upgradeInfo.newFeature);
							result.put("versionCode", upgradeInfo.versionCode);
							result.put("versionName", upgradeInfo.versionName);
							//			info.append("id: ").append(upgradeInfo.id).append("\n");
							//			info.append("标题: ").append(upgradeInfo.title).append("\n");
							//			info.append("升级说明: ").append(upgradeInfo.newFeature).append("\n");
							//			info.append("versionCode: ").append(upgradeInfo.versionCode).append("\n");
							//			info.append("versionName: ").append(upgradeInfo.versionName).append("\n");
							//			info.append("发布时间: ").append(upgradeInfo.publishTime).append("\n");
							//			info.append("安装包Md5: ").append(upgradeInfo.apkMd5).append("\n");
							//			info.append("安装包下载地址: ").append(upgradeInfo.apkUrl).append("\n");
							//			info.append("安装包大小: ").append(upgradeInfo.fileSize).append("\n");
							//			info.append("弹窗间隔（ms）: ").append(upgradeInfo.popInterval).append("\n");
							//			info.append("弹窗次数: ").append(upgradeInfo.popTimes).append("\n");
							//			info.append("发布类型（0:测试 1:正式）: ").append(upgradeInfo.publishType).append("\n");
							//			info.append("弹窗类型（1:建议 2:强制 3:手工）: ").append(upgradeInfo.upgradeType).append("\n");
							//			info.append("图片地址：").append(upgradeInfo.imageUrl);
							callbackContext.success(result);
						} catch (Exception ex) {
							callbackContext.error(ex.getMessage());
						}
					}
				}
			}

			@Override
			public void onUpgradeFailed(boolean isManual) {
				Log.d(TAG, "UPGRADE_FAILED");
			}

			@Override
			public void onUpgrading(boolean isManual) {
				Log.d(TAG, "UPGRADE_CHECKING");
			}

			@Override
			public void onDownloadCompleted(boolean b) {
				Log.d(TAG, "DownloadCompleted");
			}

			@Override
			public void onUpgradeNoVersion(boolean isManual) {
				if (callbackContext != null)
					callbackContext.success("");
			}
		};
		String appid = this.preferences.getString("android_appid", "");
		Bugly.init(this.cordova.getActivity().getApplicationContext(), appid, false);
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("checkUpgrade")) {
			this.checkUpgrade(callbackContext);
			return true;
		}
		if (action.equals("getAppInfo")) {
			this.getAppInfo(callbackContext);
			return true;
		}
		return false;
	}

	private void checkUpgrade(CallbackContext callbackContext) {
		this.callbackContext = callbackContext;
		Beta.checkUpgrade();
	}

	private void getAppInfo(CallbackContext callbackContext) {
		try {
			PackageManager packageManager = this.cordova.getActivity().getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					this.cordova.getActivity().getPackageName(), 0);
			JSONObject result = new JSONObject();
			result.put("versionName", packageInfo.versionName);
			result.put("versionCode", String.valueOf(packageInfo.versionCode));
			callbackContext.success(result);
		} catch (Exception e) {
			callbackContext.error(e.getMessage());
		}
	}
}
