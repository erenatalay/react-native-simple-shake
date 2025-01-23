import React, { useEffect } from 'react';
import { SafeAreaView, StyleSheet, Text } from 'react-native';
import { shakeListener } from 'react-native-simple-shake';

const App = (): JSX.Element => {
  useEffect(() => {
    const subscription = shakeListener(() => {
      alert('Shake event detected');
    });

    return (): void => {
      subscription.remove();
    };
  }, []);
  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.text}>Shake the device to trigger an alert</Text>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 20,
  },
  text: {
    fontSize: 20,
    marginBottom: 20,
  },
});

export default App;
