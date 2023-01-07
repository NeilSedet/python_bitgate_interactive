#include <bits/stdc++.h>
using namespace std;

#ifdef LOCAL
#include "algo/debug.h"
#define open(x) freopen(x, "r", stdin);
#else
#define open(x)
#endif

struct Node {
    bool isVisited = false, nodeValue = -1;
    int whichGate = -1;
};

// Directed graph
const int MX = 1e5;
vector<int> adj_list[MX];
Node nodeList[MX];

bool recursion(int s){
    nodeList[s].isVisited = true;

    int returnValue = -1;
    for (int u : adj_list[s]){
        if (nodeList[u].isVisited == false){
            if (returnValue == -1){
                returnValue = recursion(u);
            }else{
                returnValue = returnValue and recursion(u);
            }
        }
    }
    return returnValue;
}

void solve(){
    /* recursion() */
}

int main() {
    ios::sync_with_stdio(0); cin.tie(0);
    open("input.txt");

    int t=1;
    while(t--){
        solve();
    }
}
