# Home-Assistant-S6E-Tuya-Smart-Panel-Relay-Controls
This repository will teach you how to get the relays of the S6E Tuya Smart working with Home Assistant.

## Introduction
So you got yourself a new sweet banger Tuya S6E Android 8.1 Smart Touch Panel. Congrats, probably blew a hole in your wallet. 

You probably went through how to sideload the device and get the Home Assistant Companion app working on it, now what?

You realised your device already have relays and there is no way to get it to work. Fret not, there is a way.

## Prerequisites:

Go through these 3 guides in order by the Legend himself, Blakadder before continuing here.
1. https://blakadder.com/t6e-sideload/
2. https://blakadder.com/android-panel-webview/
3. https://blakadder.com/android-panel-proximity/

## How it works:

The Tuya S6E Panel is able to control its GPIO Pins 114 and 115 respectively for the relays through sending an echo command to its pins. This was discovered when I decompiled the test application and research how they control the Relays.

![image](https://user-images.githubusercontent.com/92814513/197569655-07def5cc-fdd2-432c-974c-3565f820dc29.png)

## How to implement:

Assuming you have completed all the guides from Blakadder, you can pick up from there.

### 1. Simply, import the relaycontrols.xml file in this repository into AutoMagic.

![WhatsApp Image 2022-10-24 at 23 53 24](https://user-images.githubusercontent.com/92814513/197570504-7836d4ed-8322-4469-abfd-c1e0d62a36fe.jpg)

![WhatsApp Image 2022-10-24 at 23 53 24](https://user-images.githubusercontent.com/92814513/197570535-c3206c82-3e3d-45e6-948d-fd97522ef338.jpg)

This will create an Automation to wait for a HTTP Request and then send an ADB Command to trigger the relays.

### 2. Test the trigger to check if it works by entering `http://IPAddressOfDevice:8080/relay1on`, `http://IPAddressOfDevice:8080/relay1off`, `http://IPAddressOfDevice:8080/relay2on`, `http://IPAddressOfDevice:8080/relay2off`

### 3. Implement a Rest Command Integration into Configuration.yaml

```  
rest_command:
  relay1on:
    url: http://192.168.2.162:8080/relay1on
  relay1off:
    url: http://192.168.2.162:8080/relay1off
  relay2on:
    url: http://192.168.2.162:8080/relay2on
  relay2off:
    url: http://192.168.2.162:8080/relay2off
```

### 4. Restart Home Assistant and check if you have the following REST services.

![image](https://user-images.githubusercontent.com/92814513/197571538-8e0fc0a1-8f40-431f-b854-e7feaf9486b3.png)

### 5. Create Template Switches to Control the Relays.

```
switch:
  - platform: template
    switches:
      s6e_relay_1:
        friendly_name: "T6E - Relay 1"
        unique_id: "t6erelay1"
        turn_on:
          service: rest_command.relay1on
        turn_off:
          service: rest_command.relay1off

      s6e_relay_2:
        friendly_name: "T6E - Relay 2"
        unique_id: "t6erelay2"
        turn_on:
          service: rest_command.relay2on
        turn_off:
          service: rest_command.relay2off
```

### 6. Reload Templates or Restart Home Assistant and you should have 2 switches to toggle the relays.

![image](https://user-images.githubusercontent.com/92814513/197572087-87bf3cf3-4009-4198-819d-c85226d8661c.png)


Now you can create a Lovelace Dashboard specially for the Switch and add in these 2 switches to control the built-in relays! Congrats!


### Disclaimer:

Its not the best method because, if the network or homeassistant goes down. You lose functionality of the switch. This can be easily countered by using the Test Application for emergency purposes or creating a new APK that executes the ADB Shell Command.

## Big Thanks to Blakadder for pointing me in the right direction on Twitter!
