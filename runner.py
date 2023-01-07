import pygame
from typing import List

# with open("input.txt", "r") as file:
#     lines = file.readlines()

#     inputValues = []
#     for line in lines:
#         line = line.rstrip()
#         line = line.split(" ")

#         inputValues.extend(line)

#     inputPointer = 0

#     def getValue():
#         global inputPointer
#         nextVal = inputValues[inputPointer]
#         inputPointer += 1

#         return int(nextVal)

#     numNodes, edgeCount = getValue(), getValue()

#     # Store a node that's the top node
#     headNode = -1
#     nodeList = []
#     adjList = [[] for i in range(numNodes)]

#     for i in range(numNodes):
#         # The status of each node
#         inputType, nodeValue = getValue(), getValue()

#         if inputType == 0:
#             node = Node(nodeValue, -1)
#         elif inputType == 1:
#             node = Node(-1, nodeValue)

#         assert inputType == 0 or inputType == 1
#         nodeList.append(node)

#     # 0 indexed
#     for i in range(edgeCount):
#         u, v = getValue(), getValue()
#         u -= 1
#         v -= 1

#         adjList[u].append(v)

#     # assert headNode != -1

#     isVisited = [False for i in range(numNodes)]
#     outputVal = nodeRecursion(headNode, nodeList, adjList, isVisited)


# Make a class that stores x and y information
class Coordinates:
    def __init__(self, x_value: int, y_value: int) -> None:
        self.x = x_value
        self.y = y_value

    def __repr__(self) -> str:
        return f"(x: {self.x}, y: {self.y})"

    def __eq__(self, other):
        if isinstance(other, Coordinates):
            return self.x == other.x and self.y == other.y
        return False


# Run a dictionary to figure out which operation to give it: use AND operation for now
class Node:
    def __init__(
        self, value: int, gate: int, neighbors: List[int], rectangle: pygame.Rect
    ) -> None:
        self.nodeValue = value
        self.whichGate = gate
        self.nodeNeighbors = neighbors
        self.drawnRectangle = rectangle

    def __repr__(self) -> str:
        # TODO
        return f"(nodeValue: {self.nodeValue}, whichGate: {self.whichGate})"


def nodeRecursion(
    currNode: int, nodeList: List[Node], adjList: List[List[int]], isVisited: List[bool]
):
    nodeList[currNode].isVisited = True

    if nodeList[currNode].nodeValue != -1:
        return nodeList[currNode].nodeValue

    returnValue = -1
    for nextNode in adjList[currNode]:

        if not isVisited[nextNode]:
            if returnValue == -1:
                returnValue = nodeRecursion(nextNode, nodeList, adjList, isVisited)
            else:
                returnValue &= nodeRecursion(nextNode, nodeList, adjList, isVisited)

    nodeList[currNode].nodeValue = returnValue
    return nodeList[currNode].nodeValue


pygame.init()
pygame.display.set_caption("Bitwise Demonstrator")

size = [800, 600]
screen = pygame.display.set_mode(size)

done = False
clock = pygame.time.Clock()
color = (255, 255, 255)

# rectangleArray: List[pygame.Rect] = []


def isWithin(rectangle: pygame.Rect, coordinates: Coordinates) -> bool:
    # print(rectangle.top, rectangle.left, rectangle.bottom, rectangle.right)
    top = rectangle.top
    left = rectangle.left
    bottom = rectangle.bottom
    right = rectangle.right

    x = coordinates.x
    y = coordinates.y
    return y >= top and y <= bottom and x >= left and x <= right


def handleMouseInRect() -> int:
    mouseState = pygame.mouse.get_pressed()
    if mouseState[0]:
        x, y = pygame.mouse.get_pos()
        newCoordinates = Coordinates(x, y)

        for i in range(len(rectangleArray)):
            if isWithin(rectangleArray[i], newCoordinates):
                return i

    return -1


isKeyPressed = False
actionQueue: List[int] = []
prevAction = -1
while not done:
    clock.tick(10)
    # Last input record

    # Exit function
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            done = True

        # Handling Keypress events
        if event.type == pygame.KEYDOWN:
            if event.key == pygame.K_SPACE:
                isKeyPressed = True

    # Handling Mouse events
    size = (50, 50)
    mouseState = pygame.mouse.get_pressed()
    if mouseState[0]:
        if isKeyPressed:
            x, y = pygame.mouse.get_pos()
            newRectangle = pygame.Rect(x, y, size[0], size[1])
            # rectangleArray.append(newRectangle)

            isKeyPressed = False

        currAction = handleMouseInRect()
        if currAction != -1:
            if prevAction != -1:
                pass
                # TODO Might be backwards
                # adjList[prevAction] = currAction
                # Render Node
                # Make a line between prevAction and whichRectangle
            # actionQueue.append(whichRectangle)

    # Draw background
    screen.fill(color)

    for rectangle in rectangleArray:
        # textRect = .get_rect()

        # # Set the center of the rectangular object.
        # textRect.center = (X // 2, Y // 2)

        pygame.draw.rect(screen, (0, 0, 0), rectangle)

    # This function must write after all the other drawing commands.
    pygame.display.flip()

# Quite the execution when clicking on close
pygame.quit()
