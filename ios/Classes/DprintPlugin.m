#import "DprintPlugin.h"
#import <dprint/dprint-Swift.h>

@implementation DprintPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftDprintPlugin registerWithRegistrar:registrar];
}
@end
