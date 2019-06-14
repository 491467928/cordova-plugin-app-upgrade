# cordova-plugin-app-upgrade
version：1.0.1

cordova app版本升级插件，
android集成了腾讯的bugly的升级框架，需要去注册一个appid，官网地址：https://bugly.qq.com/v2/
ios需要登录appstore的发布页面，拿到appid

#安装
cordova plugin add cordova-plugin-app-upgrade  --variable android_appid=bugly的appId --variable ios_appid=appstore的appid

#用法
检测更新
```Javascript
AppUpgrade.checkUpgrade(function(){
	//成功，返回服务端的更新包信息
	//{
	//	versionCode:'版本号，例如102',
	//	versionName:'版本名称，例如1.0.2',
	//	newFeature:'更新说明'
	//}
	
},function(e) {
  //失败
})
```


获取当前版本信息
```Javascript
AppUpgrade.getAppInfo(function(){
	//成功，返回服务端的更新包信息
	//{
	//	versionCode:'版本号，例如102',
	//	versionName:'版本名称，例如1.0.2'
	//}
	
},function(e) {
  //失败
})
```