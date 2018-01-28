from user_class import *
# from vlocc import *
from flask import Flask
import requests


destination = "https://zccrzblqzc.localtunnel.me"



def send_to():
    """
    Functionality for sending attachments for other (WIP)
    """
    recieve = input("Please enter VlocC address of other user: ")
    attachment = input("Please enter what you want to send: ")
    User.send(recieve, attachment)

def make_new_user():
    """
    Pings website to create a new user
    """
    ping = requests.get(destination+"/newuser")
    print(ping.text)
    return ping.text

def main():
    print("Welcome to VlocC!")
    new_user = input("Are you a new user? (y/n): ")
    if new_user == "y":
            new_name = make_new_user()
            print("Your Username/Address is", new_name)
    user_name = input("Please enter username: ")
    
    r = requests.put(destination+"/verify", data=({'Username':user_name})
    r = requests.get(destination+"/verify")

    while(bool(r.text) == False):
        print("Invalid Username")
        user_name = input("Please enter username: ")
    print("Username accepted!")

    contin = "y"
    while contin == "y": # Loops until user inputs otherwise
            while True:
                    userinput = input("What would you like to do today? ('help' for all options) ")
                    if userinput == "help":
                            print("send, view, last, wallet")
                    else:
                            break
            if userinput == "send":
                    send_to()
            elif userinput == "view":
                user_exchanges = requests.put(destination,data({'User-Exchanges':exchanges}))
                for i in user_exchanges.headers['User-Exchanges']
            elif userinput == "last":
                    print(user_exchanges[-1])
            elif userinput == "wallet":
                    print(User.view_wallet())
            contin = input("Would you like to continue? (y/n): ") 

if __name__ == '__main__':
    main()
