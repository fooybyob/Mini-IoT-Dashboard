


import paho.mqtt.client as mqtt
import json, time, random,sys

interval = 2   # seconds between readings — can be changed by a command
sensor=sys.argv[1]

def on_message(client, userdata, msg):
    global interval
    try:
        payload = msg.payload.decode()
        interval = int(payload)
        print("Command received — interval is now", interval, "seconds")
    except Exception as e:
        print("Bad command:", e)

c = mqtt.Client()
c.connect("localhost", 1883)

c.on_message = on_message
c.subscribe("sensors/"+sensor+"/command")
c.loop_start()   # run the listener on a background thread

while True:
    payload = json.dumps({
        "sensorId": sensor,
        "temperature": round(random.uniform(18, 40), 1),
        "timestamp": time.strftime("%Y-%m-%d %H:%M:%S"),
        "timestampLong": int(time.time() * 1000)
    })
    c.publish("sensors/"+sensor+"/temperature", payload)
    print("sent", payload)
    time.sleep(interval)