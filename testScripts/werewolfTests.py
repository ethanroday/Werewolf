import requests
import json

users = [
    {
	"firstName":"John",
	"lastName":"Smith",
	"userName":"john",
	"password":"123456",
	"imageURL":"",
	"roles":["ROLE_USER"],
	"score":0
    },{   
	"firstName":"Fred",
	"lastName":"Thompson",
	"userName":"fred",
	"password":"123456",
	"imageURL":"",
	"roles":["ROLE_USER"],
	"score":0
    },{
	"firstName":"Hillary",
	"lastName":"Williams",
	"userName":"hill",
	"password":"123456",
	"imageURL":"",
	"roles":["ROLE_USER"],
	"score":0
    },{
	"firstName":"Jane",
	"lastName":"Doe",
	"userName":"jane",
	"password":"123456",
	"imageURL":"",
	"roles":["ROLE_USER"],
	"score":0
    },{
	"firstName":"Harry",
	"lastName":"Truman",
	"userName":"harry",
	"password":"123456",
	"imageURL":"",
	"roles":["ROLE_USER"],
	"score":0
    },{
	"firstName":"Lisa",
	"lastName":"Brown",
	"userName":"lisa",
	"password":"123456",
	"imageURL":"",
	"roles":["ROLE_USER"],
	"score":0
    },{
	"firstName":"Alexa",
	"lastName":"Smith",
	"userName":"alexa",
	"password":"123456",
	"imageURL":"",
	"roles":["ROLE_USER"],
	"score":0
    },{
	"firstName":"Sandra",
	"lastName":"Jones",
	"userName":"sonny",
	"password":"123456",
	"imageURL":"",
	"roles":["ROLE_USER"],
	"score":0
    },{
	"firstName":"Barack",
	"lastName":"Obama",
	"userName":"prez",
	"password":"123456",
	"imageURL":"",
	"roles":["ROLE_USER"],
	"score":0
    }]
ethan = {
	"firstName":"Ethan",
	"lastName":"Roday",
	"userName":"ethan",
	"password":"123456",
	"imageURL":"",
	"roles":["ROLE_USER","ROLE_ADMIN"],
	"score":0
    }

#Each is in kill range of the next; scent range
#of two down
locations = [(37.270215,-76.709800),
             (37.270215,-76.709825),
             (37.270215,-76.709850),
             (37.270215,-76.709875),
             (37.270215,-76.709900),
             (37.270215,-76.709925),
             (37.270215,-76.709950),
             (37.270215,-76.709975),
             (37.270215,-76.710000)]
location_ethan = (37.270215,-76.710025)

ROOT = "http://murmuring-cliffs-5802.herokuapp.com/"
ADMIN_AUTH = ("ethan","123456")

def getAuth(user):
    return (user["userName"],user["password"])

def clearAllData():
    url = ROOT+"clearAllData"
    print("Removing all data at %s\n\t" % url)
    r = requests.get(url,auth=ADMIN_AUTH)
    if not r.ok:
        r.raise_for_status()
    elif r.json()["status"] != "OK":
        e = requests.exceptions.HTTPError(r.json()["status"])
        raise e
    else:
        print("Housekeeping is finished.")

def addUser(user):
    url = ROOT+"users/add"
    print("Setting up request for user with username %s at\n\t%s" % (user["userName"],url))
    r = requests.post(url,data=user,auth=ADMIN_AUTH)
    if not r.ok:
        r.raise_for_status()
    elif r.json()["status"] != "OK":
        e = requests.exceptions.HTTPError(r.json()["status"])
        raise e
    else:
        print("Successfully added user with username %s" % user["userName"])

def startGame(dayNight,scent,kill,auth):
    params = {"dayNightFreq":dayNight,"scentRange":scent,"killRange":kill}
    url = ROOT+"restart"
    print("Setting up request for game with dayNight = %d, scentRange = %d, killRange = %d at\n\t%s" %
          (dayNight,scent,kill,url))
    print("Authorization is user:%s, password:%s" % (auth[0],auth[1]))
    r = requests.post(url,data=params,auth=auth)
    if not r.ok:
        r.raise_for_status()
    elif r.json()["status"] != "OK":
        e = requests.exceptions.HTTPError(r.json()["status"])
        raise e
    else:
        print("Successfully started the game.")

def updateLocation(user,loc):
    params = {"lng":loc[1],"lat":loc[0]}
    url = ROOT+"location"
    r = requests.post(url,data=params,auth=getAuth(user))
    if not r.ok:
        r.raise_for_status()
    elif r.json()["status"] != "OK":
        e = requests.exceptions.HTTPError(r.json()["status"])
        raise e
    else:
        print("Updated %s's location to %f,%f" % (user["userName"],loc[0],loc[1]))

def getPlayerDetails(user):
    url = ROOT+"players/me"
    r = requests.get(url,auth=getAuth(user))
    if not r.ok:
        r.raise_for_status()
    elif r.json()["status"] != "OK":
        e = requests.exceptions.HTTPError(r.json()["status"])
        raise e
    else:
        return r.json()["object"]

def getUserDetails(user):
    url = ROOT+"users/me"
    r = requests.get(url,auth=getAuth(user))
    if not r.ok:
        r.raise_for_status()
    elif r.json()["status"] != "OK":
        e = requests.exceptions.HTTPError(r.json()["status"])
        raise e
    else:
        return r.json()["object"]

def getNearbyPlayers(user):
    url = ROOT+"nearby"
    print("Setting up request for players near %s (scent range) at \n\t%s" % (user["userName"],url))
    r = requests.get(url,auth=getAuth(user))
    if not r.ok:
        r.raise_for_status()
    elif r.json()["status"] != "OK":
        e = requests.exceptions.HTTPError(r.json()["status"])
        raise e
    else:
        return r.json()["object"]

def getKillablePlayers(user):
    url = ROOT+"killable"
    print("Setting up request for townspeople near %s (kill range) at \n\t%s" % (user["userName"],url))
    r = requests.get(url,auth=getAuth(user))
    if not r.ok:
        r.raise_for_status()
    elif r.json()["status"] != "OK":
        print(dumpJSON(r.json()["object"]))
        e = requests.exceptions.HTTPError(r.json()["status"])
        raise e
    else:
        return r.json()["object"]

def kill(killerUser,victimPlayer):
    url = ROOT+"kill"
    print("Setting up request for kill with username %s at\n\t%s" % (killerUser["userName"],url))
    params = {"id":victimPlayer["id"]}
    r = requests.post(url,data=params,auth=getAuth(killerUser))
    if not r.ok:
        r.raise_for_status()
    elif r.json()["status"] != "OK":
        e = requests.exceptions.HTTPError(r.json()["status"])
        raise e
    else:
        print("Player with id %s has been killed by %s." % (victimPlayer["id"],killerUser["userName"]))

def vote(voterUser,accusedPlayer):
    url = ROOT+"vote"
    params = {"id":accusedPlayer["id"]}
    r = requests.post(url,data=params,auth=getAuth(voterUser))
    if not r.ok:
        r.raise_for_status()
    elif r.json()["status"] != "OK":
        e = requests.exceptions.HTTPError(r.json()["status"])
        raise e
    else:
        print("Player with id %s has been voted on by %s." % (accusedPlayer["id"],voterUser["userName"]))

def getAllAlive():
    url = ROOT+"players/alive"
    r = requests.get(url,auth=ADMIN_AUTH)
    if not r.ok:
        r.raise_for_status()
    elif r.json()["status"] != "OK":
        e = requests.exceptions.HTTPError(r.json()["status"])
        raise e
    else:
        return r.json()["object"]

def forceNight():
    url = ROOT+"forceNight"
    r = requests.get(url,auth=ADMIN_AUTH)

def dumpJSON(serialized):
    return json.dumps(serialized, sort_keys=True, indent=4, separators=(',', ': '))

def main():
    print("Welcome to the Werewolf testing scripts. These scripts test the app deployed at")
    print("http://murmuring-cliffs-5802.herokuapp.com/. There is a single administrator (named 'ethan')")
    print("in the database whose authentication details are hard-coded into this script.")
    print("To begin, press Enter.\n")
    input()
    print("First, for the purposes of testing, we're going to clear the entire database except")
    print("for the requesting user ('ethan'). If you do NOT want to proceed with clearing the")
    raw = input("database, enter a period to quit:")
    if raw == ".":
        print("Exiting.")
        exit()
    clearAllData()
    print()
    
    print("We begin by adding eight users to the database (all of which have only ROLE_USER):")
    for i in range(len(users)-1):
        try:
            addUser(users[i])
        except Exception as e:
            print("An error occurred:",e)
    print("Now that we've added some users, we'll attempt to start a game (press Enter):\n")
    input()
    try:
        startGame(15,6,3,getAuth(users[0]))
    except Exception as e:
        print("An error occurred:",e)
    print("Notice how we got an error, since %s doesn't have the proper authorization." % users[0]['userName'])
    print("Now we'll authorize with an admin (press Enter):\n")
    input()
    try:
        startGame(15,6,3,ADMIN_AUTH)
    except Exception as e:
        print("An error occurred:",e)
    print("This time, we didn't have enough players.\nSo, we'll add one more user, which will be enough (press Enter):\n")
    input()
    try:
        addUser(users[-1])
    except Exception as e:
        print("An error occurred:",e)    
    print("Starting the game for a third time, things go smoothly (press Enter):\n")
    input()
    try:
        startGame(15,6,3,ADMIN_AUTH)
    except Exception as e:
        print("An error occurred:",e)
    print()
    
    print("Now, we'll update locations for all of the players. The locations are hard-coded - each player is in killing")
    print("distance of at least one other player, and within scent range of at least one player (press Enter):\n")
    input()
    for i in range(len(users)):
        try:
            updateLocation(users[i],locations[i])
        except Exception as e:
            print("An error occurred:",e)
    try:
        updateLocation(ethan,location_ethan)
    except Exception as e:
        print("An error occurred:",e)
        
    print("Finished updating locations. Press Enter to continue.\n")
    input()
    werewolfUser = None
    werewolfPlayer = None
    townspersonUser = None
    townspersonPlayer = None
    i = 0
    j = 0
    print("Looking through our players, we find one user and one werewolf.")
    while werewolfUser == None or townspersonUser == None:
        player = getPlayerDetails(users[i])
        if player["isWerewolf"] and werewolfUser == None:
            werewolfUser = getUserDetails(users[i])
            werewolfPlayer = player
        else: i += 1
        if not player["isWerewolf"] and townspersonUser == None:
            townspersonUser = getUserDetails(users[j])
            townspersonPlayer = player
        else: j += 1
    print("Here is our werewolf:")
    print(dumpJSON(werewolfPlayer))
    print(dumpJSON(werewolfUser))
    print("Now let's have this werewolf kill (press Enter).\n")
    input()
    killable = getKillablePlayers(werewolfUser)
    try: kill(werewolfUser,killable[0])
    except Exception as e: print("An error occured:",e)
    print("It's day, so the werewolf can't kill. But the townspeople can vote.\n")
    print("So we have a townsperson vote (incidentally, for the aforementioned werewolf).\nNotice how the votedFor field is updated (press Enter).\n")
    input()
    print("Before:")
    print(dumpJSON(townspersonPlayer))
    vote(townspersonUser,werewolfPlayer)
    print("After:")
    print(dumpJSON(getPlayerDetails(townspersonUser)))
    print("For the purposes of testing, now force it to be night (an administrative operation) (press Enter).\n")
    input()
    forceNight()
    print("Now that it's night, the werewolf tries to kill again (press Enter).\n")
    input()
    try: kill(werewolfUser,killable[0])
    except Exception as e: print("An error occured:",e  )
    print("The werewolf can't kill, though, because it's dead! When the day ended, it was killed because it had the most votes.")
    print("So, we'll find another werewolf (press Enter):\n")
    input()
    i += 1
    secondWerewolfUser = None
    secondWerewolfPlayer = None
    while secondWerewolfUser == None:
        player = getPlayerDetails(users[i])
        if player["isWerewolf"] and secondWerewolfUser == None:
            secondWerewolfUser = getUserDetails(users[i])
            secondWerewolfPlayer = player
        else: i += 1
    print("Here is our second werewolf:")
    print(dumpJSON(secondWerewolfPlayer))
    print(dumpJSON(secondWerewolfUser))
    input()
    print("This werewolf has the perfect killing conditions: it's night, and the werewolf is alive (press Enter).\n")
    killable = getKillablePlayers(secondWerewolfUser)
    kill(secondWerewolfUser,killable[0])

    print("\nNotice how, when we ask for all alive players, %s (the dead werewolf) and %s (the just-killed townsperson) are missing."
          % (werewolfUser["userName"],killable[0]["userName"]))
    print("Also note that the killing werewolf's score has gone up, since he/she has been rewarded for the kill (press Enter).\n")
    input()
    print(dumpJSON(getAllAlive()))
    print("This concludes the demonstration of web service functionality for the Werewolf game. Press Enter to exit.")
    input()
    exit()
    
main()

