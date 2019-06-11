package cordova-plugin-app-upgrade;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class AppUpgrade extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("checkUpgrade")) {
            this.coolMethod(callbackContext);
            return true;
        }
        return false;
    }

    private void checkUpgrade(CallbackContext callbackContext) {
        callbackContext.success("ok");
    }
}
