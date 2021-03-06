1.创建节点


- 创建元素节点:document.createElement(tagName);

var liNode = document.createElement("li");

- 创建属性节点：先需要创建一个元素节点，然后通过 . 的方式为其属性赋值

var aNode = document.createElement("a");

aNode.href = "deleteEmp?Id=xxx";

- 创建文本节点:document.createTextNode(textContent);

var xmText = document.createTextNode("厦门");

- 注意:新创建的节点不会自动加入文档树的任意位置，需要调用节点的appendChild()方法把新创建的节点加入到文档树合适的位置


2.把节点加入指定节点的最后一个子结点

```
//document.createElement(elementTagName);
//1.新创建一个元素节点，返回值为指向元素节点的引用
var liNode = document.createElement("li");
//<li></li>

//2.创建"厦门"文本节点
//document.createTextNode(String)创建一个文本节点,参数为文本值，返回该文本节点的引用
var xmText = document.createTextNode("厦门");
//<li>厦门</li>

liNode.appendChild(xmText);

var cityNode = document.getElementById("city");
//elementNode.appendChild(newChild):为elementNode新添加newNode子结点，该子结点将作为elementNode的最后一个子结点
cityNode.appendChild(liNode);
```

3.节点的替换:replaceChild(newChild,oldChild)

- 该方法除了替换外，还有移动节点的功能

- 代码:

```
var bjNode = document.getElementById("bj");
var rlNode = document.getElementById("rl");
var gameNode = document.getElementById("game");

//将把rlNode替换为bjNode，同时bjNode被移动
gameNode.replace(bjNode,rlNode);
```

- 可以自定义一个replaceEach(aNode,bNode)实现a和b的呼唤

```
function replaceEach(aNode,bNode){
	//1.获取aNode和bNode的父结点，使用parentNode属性
	var aParent = aNode.parentNode;
	var bParent = bNode.parentNode;
	
	if(aParent && bParent){
		//2.克隆aNode或bNode
		var aNode2 = aNode.cloneNode(true);					
		//3.分别调用aNode的父结点和bNode的父结点的replaceChild()实现互换
		bParent.replaceChild(aNode2,bNode);
		aParent.replaceChild(bNode,aNode);
		
	}
	
}
```

4.移除节点:removeChild(refChild)

- 可以借助parentNode属性

- 代码

```
var bjNode = document.getElementById("bj");
bjNode.parentMode.removeChild(bjNode);
```

5.节点的插入：insertBefore(newNode,refNode)

- 若newNode是文档中的节点，也具有移动节点的功能

- 代码

```
var cityNode = document.getElementById("city");
var bjNode = document.getElementById("bj");
var rlNode = document.getElementById("rl");

//cityNode.insertBefore(rlNode, bjNode);
```

- W3C没有定义insertAfter方法,自定义insertAfter(newNode,refNode)：实现把newNode加入为refNode之后

```
//把newNode插入到refNode的后面
function insertAfter(newNode,refNode){
	//1.测试refNode是否为其父结点的最后一个子结点
	var parentNode = refNode.parentNode;
	if(parentNode){
		var lastNode = parentNode.lastChild;
		//2.若是:直接把newNode插入refNode父结点的最后一个子结点
		if(refNode == lastNode){
			parentNode.appendChild(newNode);
		}else{
		//3.若不是:获取refNode的下一个兄弟节点，然后插入其下一个兄弟节点的前面
			var nextNode = refNode.nextSibling;
			parentNode.insertBefore(newNode,nextNode);
		}
	}
	
}
```

6.innerHTML属性

- 非标准，但所有的浏览器都支持

- 读写属性，读写某HTML元素的内容

- 代码

```
//互换city节点和#game节点中的内容
var tempHTML = cityNode.innerHTML;
var gameNode = document.getElementById("game");

cityNode.innerHTML = gameNode.innerHTML;
gameNode.innerHTML = tempHTML;
```








