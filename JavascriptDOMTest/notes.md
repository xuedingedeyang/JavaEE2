1.�����ڵ�


- ����Ԫ�ؽڵ�:document.createElement(tagName);

var liNode = document.createElement("li");

- �������Խڵ㣺����Ҫ����һ��Ԫ�ؽڵ㣬Ȼ��ͨ�� . �ķ�ʽΪ�����Ը�ֵ

var aNode = document.createElement("a");

aNode.href = "deleteEmp?Id=xxx";

- �����ı��ڵ�:document.createTextNode(textContent);

var xmText = document.createTextNode("����");

- ע��:�´����Ľڵ㲻���Զ������ĵ���������λ�ã���Ҫ���ýڵ��appendChild()�������´����Ľڵ���뵽�ĵ������ʵ�λ��


2.�ѽڵ����ָ���ڵ�����һ���ӽ��

```
//document.createElement(elementTagName);
//1.�´���һ��Ԫ�ؽڵ㣬����ֵΪָ��Ԫ�ؽڵ������
var liNode = document.createElement("li");
//<li></li>

//2.����"����"�ı��ڵ�
//document.createTextNode(String)����һ���ı��ڵ�,����Ϊ�ı�ֵ�����ظ��ı��ڵ������
var xmText = document.createTextNode("����");
//<li>����</li>

liNode.appendChild(xmText);

var cityNode = document.getElementById("city");
//elementNode.appendChild(newChild):ΪelementNode������newNode�ӽ�㣬���ӽ�㽫��ΪelementNode�����һ���ӽ��
cityNode.appendChild(liNode);
```

3.�ڵ���滻:replaceChild(newChild,oldChild)

- �÷��������滻�⣬�����ƶ��ڵ�Ĺ���

- ����:

```
var bjNode = document.getElementById("bj");
var rlNode = document.getElementById("rl");
var gameNode = document.getElementById("game");

//����rlNode�滻ΪbjNode��ͬʱbjNode���ƶ�
gameNode.replace(bjNode,rlNode);
```

- �����Զ���һ��replaceEach(aNode,bNode)ʵ��a��b�ĺ���

```
function replaceEach(aNode,bNode){
	//1.��ȡaNode��bNode�ĸ���㣬ʹ��parentNode����
	var aParent = aNode.parentNode;
	var bParent = bNode.parentNode;
	
	if(aParent && bParent){
		//2.��¡aNode��bNode
		var aNode2 = aNode.cloneNode(true);					
		//3.�ֱ����aNode�ĸ�����bNode�ĸ�����replaceChild()ʵ�ֻ���
		bParent.replaceChild(aNode2,bNode);
		aParent.replaceChild(bNode,aNode);
		
	}
	
}
```

4.�Ƴ��ڵ�:removeChild(refChild)

- ���Խ���parentNode����

- ����

```
var bjNode = document.getElementById("bj");
bjNode.parentMode.removeChild(bjNode);
```

5.�ڵ�Ĳ��룺insertBefore(newNode,refNode)

- ��newNode���ĵ��еĽڵ㣬Ҳ�����ƶ��ڵ�Ĺ���

- ����

```
var cityNode = document.getElementById("city");
var bjNode = document.getElementById("bj");
var rlNode = document.getElementById("rl");

//cityNode.insertBefore(rlNode, bjNode);
```

- W3Cû�ж���insertAfter����,�Զ���insertAfter(newNode,refNode)��ʵ�ְ�newNode����ΪrefNode֮��

```
//��newNode���뵽refNode�ĺ���
function insertAfter(newNode,refNode){
	//1.����refNode�Ƿ�Ϊ�丸�������һ���ӽ��
	var parentNode = refNode.parentNode;
	if(parentNode){
		var lastNode = parentNode.lastChild;
		//2.����:ֱ�Ӱ�newNode����refNode���������һ���ӽ��
		if(refNode == lastNode){
			parentNode.appendChild(newNode);
		}else{
		//3.������:��ȡrefNode����һ���ֵܽڵ㣬Ȼ���������һ���ֵܽڵ��ǰ��
			var nextNode = refNode.nextSibling;
			parentNode.insertBefore(newNode,nextNode);
		}
	}
	
}
```

6.innerHTML����

- �Ǳ�׼�������е��������֧��

- ��д���ԣ���дĳHTMLԪ�ص�����

- ����

```
//����city�ڵ��#game�ڵ��е�����
var tempHTML = cityNode.innerHTML;
var gameNode = document.getElementById("game");

cityNode.innerHTML = gameNode.innerHTML;
gameNode.innerHTML = tempHTML;
```







