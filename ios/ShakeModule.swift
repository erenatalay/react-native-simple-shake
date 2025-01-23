import React
import UIKit

@objc(ShakeModule)
class ShakeModule: RCTEventEmitter {
    override static func requiresMainQueueSetup() -> Bool {
        return true
    }
    
    override func supportedEvents() -> [String]! {
        return ["ShakeDetected"]
    }
    
    override func startObserving() {
        UIApplication.shared.applicationSupportsShakeToEdit = true
    }
    
    override func stopObserving() {
        UIApplication.shared.applicationSupportsShakeToEdit = false
    }
    
    func sendShakeEvent() {
        sendEvent(withName: "ShakeDetected", body: nil)
    }
}

extension UIViewController {
    open override func motionEnded(_ motion: UIEvent.EventSubtype, with event: UIEvent?) {
        guard motion == .motionShake,
              let rootView = UIApplication.shared.delegate?.window??.rootViewController?.view else {
            return
        }
        
        if let bridgeModule = RCTBridge.current().module(for: ShakeModule.self) as? ShakeModule {
            bridgeModule.sendShakeEvent()
        }
    }
}
