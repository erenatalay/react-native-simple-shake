import {
  NativeModules,
  Platform,
  NativeEventEmitter,
  type EmitterSubscription,
} from 'react-native';

import type { ShakeCallback } from './shake.types';

export * from './shake.types';
const LINKING_ERROR =
  `The package 'react-native-simple-biometrics' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const ShakeModule = NativeModules.ShakeModule
  ? NativeModules.ShakeModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export const shakeListener = (onShake: ShakeCallback): EmitterSubscription => {
  const shakeEmitter = new NativeEventEmitter(ShakeModule);
  const subscription = shakeEmitter.addListener('ShakeDetected', onShake);
  return subscription;
};
