# 屏幕取色  
### 问题解决  

1. __截图界面错位或模糊：__  
添加VM配置 `-Dsun.java2d.uiScale=1`

2. __Windows下中文乱码：__  
添加VM配置 `-Dfile.encoding=GBK`

3. __resources加载出错__  
在Artifacts里把resources添加到Directory Content
