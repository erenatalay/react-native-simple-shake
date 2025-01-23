#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ShakeModule, NSObject)
RCT_EXTERN_METHOD(startObserving)
RCT_EXTERN_METHOD(stopObserving)
@end
