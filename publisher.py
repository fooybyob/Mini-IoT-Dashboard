# publisher.py
import paho.mqtt.client as mqtt, json, time, random
c = mqtt.Client()
c.connect("localhost", 1883)
while True:
    payload = json.dumps({"sensorId":"sensor-1","temperature":round(random.uniform(18,40),1),"timestamp":time.strftime("%Y-%m-%d %H:%M:%S")})
    c.publish("sensors/sensor-1/temperature", payload)
    print("sent", payload)
    time.sleep(2)