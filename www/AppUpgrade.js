var exec = require('cordova/exec');

exports.checkUpgrade = function (success, error) {
    exec(success, error, 'AppUpgrade', 'checkUpgrade', []);
};
exports.getAppInfo = function (success, error) {
	exec(success, error, 'AppUpgrade', 'getAppInfo', []);
};
