import tkinter as tk
from PIL import ImageTk, Image
import time

# make some sort of gui
# get mouse input
# get coordinates of that
# get image displayed at that coordinate
# process each image class and the inputs
# do bitwise operations and display the final result

# keep track of which image object connects to which one

# make an array of locations
# make each index an identifying number 'id'


# make a method of 'selecting'
# for each location, check if the mouse is within the bounds by using the resize parameters
# then return the object id that it is selecting

# make a menu to change functions

window = tk.Tk()
window.title("TEST")
window.geometry("700x900")
# window.attributes('-fullscreen',True)


#     img = Image.open(path).resize((40, 40))
def imgCP(path):
    photo_image = ImageTk.PhotoImage(img)
    image_obj = lambda: tk.Label(window, image=photo_image)
#     # return tk.Label(window, image=photo_image)

#     return image_obj()

# img = Image.open("./bin/gate_images/norGate.png").resize((40, 40))
# photo_image = ImageTk.PhotoImage(img)
# image_obj = lambda: tk.Label(window, image=photo_image)

# clickButton = False
# def orButton():
#     global clickButton
#     clickButton = True
#     global image_obj
#     img = Image.open("./bin/gate_images/norGate.png").resize((40, 40))
#     photo_image = ImageTk.PhotoImage(img)
#     image_obj = lambda: tk.Label(window, image=photo_image)


# orButton = tk.Button(
#     window,
#     text="Red",
#     command=orButton,
# )
# orButton.pack(side=tk.BOTTOM)


class imgObjOR:
    def __init__(self, x, y):
        self.x = x
        self.y = y
        self.in1 = -1
        self.in2 = -1
        self.id = -1

    def __repr__(self):
        return self.x + " " + self.y + " " + self.in1 + " " + self.in2 + " " + self.id

    def output(self):
        if self.in1 == -1 or self.in2 == -1:
            return -1
        return self.in1 or self.in2


locations = []
currPath = "./IMG.jpeg"


def motion(event):
    x, y = event.x, event.y
    # global image_obj

    img = imgCP(currPath)
    img.place(x=x, y=y)

    # try:
    # image_obj.place(x=x, y=y)
    # image_obj().place(x=x, y=y)
    new_obj = imgObjOR(x, y)
    locations.append(new_obj)

    print("{}, {}".format(x, y))
    # except:

    #     print("Select an option dipshit")


window.bind("<Button 1>", motion)
tk.mainloop()
