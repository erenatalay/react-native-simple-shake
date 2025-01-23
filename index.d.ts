import type { EmitterSubscription } from 'react-native';

import type { ShakeCallback } from './src';

declare module 'react-native-simple-shake' {
  export function shakeListener(onShake: ShakeCallback): EmitterSubscription;
}
