/********* AppUpgrade.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>

@interface AppUpgrade : CDVPlugin {
    // Member variables go here.
}

- (void)checkUpgrade:(CDVInvokedUrlCommand*)command;
- (void)getAppInfo:(CDVInvokedUrlCommand*)command;
-(NSInteger) versionNameToCode:(NSString*) name;
@end

@implementation AppUpgrade

- (void)checkUpgrade:(CDVInvokedUrlCommand*)command
{
    NSString* appid=[[self.commandDelegate settings] objectForKey:@"ios_appid"];
    NSString* url=[[NSString alloc] initWithFormat:@"https://itunes.apple.com/cn/lookup?id=%@",appid];
    NSString* currentVersion=[NSBundle mainBundle].infoDictionary[@"CFBundleShortVersionString"];
    NSURL* requestUrl=[NSURL URLWithString:url];
    NSURLSession* session=[NSURLSession sharedSession];
    NSURLSessionDataTask* dataTask=[session dataTaskWithURL:requestUrl completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        
        NSDictionary* dict=[NSJSONSerialization JSONObjectWithData:data
                                                           options:kNilOptions
                                                             error:nil];
        CDVPluginResult* pluginResult=nil;
        if(dict[@"results"]){
            NSString* version=dict[@"results"][0][@"version"];
            NSString* releasNote=dict[@"results"][0][@"releaseNotes"] ;
            NSMutableDictionary* resultDic=[[NSMutableDictionary alloc] init];
            [resultDic setObject:version forKey:@"versionName"];
            [resultDic setObject:releasNote forKey:@"newFeature"];
            NSInteger currentVersionCode=[self versionNameToCode:currentVersion];
            NSInteger upgradeVersionCode=[self versionNameToCode:version];
            [resultDic setObject:[NSString stringWithFormat:@"%ld",upgradeVersionCode] forKey:@"versionCode"];
            if(upgradeVersionCode>currentVersionCode){
                UIAlertController *alert=[UIAlertController alertControllerWithTitle:[NSString stringWithFormat:@"发现新版本%@",version] message:releasNote preferredStyle:UIAlertControllerStyleAlert];
                UIAlertAction* ok=[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleDefault handler:nil];
                UIAlertAction *update=[UIAlertAction actionWithTitle:@"去更新" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                    NSString* urlStr=[NSString stringWithFormat:@"itms-apps://itunes.apple.com/cn/app/id%@?mt=8",appid];
                    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:urlStr] options:@{} completionHandler:nil];
                }];
                [alert addAction:ok];
                [alert addAction:update];
                [self.viewController presentViewController:alert animated:YES completion:nil];
                pluginResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:resultDic];
            }
        }else{
            pluginResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@""];
        }
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
    [dataTask resume];
}
-(void)getAppInfo:(CDVInvokedUrlCommand *)command
{
    NSString* currentVersion=[NSBundle mainBundle].infoDictionary[@"CFBundleShortVersionString"];
    NSInteger versionCode=[self versionNameToCode:currentVersion];
    NSMutableDictionary* resultDic=[[NSMutableDictionary alloc] init];
    [resultDic setObject:currentVersion forKey:@"versionName"];
    [resultDic setObject:[NSString stringWithFormat:@"%ld",versionCode] forKey:@"versionCode"];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:resultDic];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(NSInteger) versionNameToCode:(NSString*) name
{
    NSArray* array=[name componentsSeparatedByString:@"."];
    NSInteger versionCode=0;
    if(array.count==3){
        versionCode=[array[0] integerValue]*100+[array[1] integerValue]*10+[array[2] integerValue];
    }
    return versionCode;
}

@end


