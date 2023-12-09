import{_ as s,o as n,c as a,a as l}from"./app.c0262cae.js";const h=JSON.parse('{"title":"前端部署","description":"","frontmatter":{},"headers":[{"level":3,"title":"配置前端文件","slug":"配置前端文件","link":"#配置前端文件","children":[]},{"level":3,"title":"打包","slug":"打包","link":"#打包","children":[]},{"level":3,"title":"部署","slug":"部署","link":"#部署","children":[]}],"relativePath":"guide/web.md"}'),e={name:"guide/web.md"},p=l(`<h1 id="前端部署" tabindex="-1">前端部署 <a class="header-anchor" href="#前端部署" aria-hidden="true">#</a></h1><div class="tip custom-block"><p class="custom-block-title">TIP</p><p>前端使用vue3开发</p></div><h3 id="配置前端文件" tabindex="-1">配置前端文件 <a class="header-anchor" href="#配置前端文件" aria-hidden="true">#</a></h3><p>前端文件位于<code>client\\utils\\src\\config.ts</code>，修改<code>domain</code>为后端域名+端口，如<code>xxx.xxx.xxx.xxx:2023</code>，如果是本地部署可以使用<code>localhost:2023</code></p><h3 id="打包" tabindex="-1">打包 <a class="header-anchor" href="#打包" aria-hidden="true">#</a></h3><p>前端分为web-client、manage-client两个项目，分别对应web端、后台管理端：</p><p>先安装依赖</p><div class="language-"><button title="Copy Code" class="copy"></button><span class="lang"></span><pre class="shiki material-palenight"><code><span class="line"><span style="color:#A6ACCD;"># 进入client目录</span></span>
<span class="line"><span style="color:#A6ACCD;">cd client</span></span>
<span class="line"><span style="color:#A6ACCD;"># 安装依赖</span></span>
<span class="line"><span style="color:#A6ACCD;">pnpm i</span></span>
<span class="line"><span style="color:#A6ACCD;"></span></span></code></pre></div><p>如果没有安装pnpm，可以使用npm代替或者安装pnpm</p><div class="language-"><button title="Copy Code" class="copy"></button><span class="lang"></span><pre class="shiki material-palenight"><code><span class="line"><span style="color:#A6ACCD;"># 安装pnpm</span></span>
<span class="line"><span style="color:#A6ACCD;">npm i -g pnpm</span></span>
<span class="line"><span style="color:#A6ACCD;"></span></span></code></pre></div><p>打包web-client</p><div class="language-"><button title="Copy Code" class="copy"></button><span class="lang"></span><pre class="shiki material-palenight"><code><span class="line"><span style="color:#A6ACCD;"># 进入web-client目录</span></span>
<span class="line"><span style="color:#A6ACCD;">cd packages/web-client</span></span>
<span class="line"><span style="color:#A6ACCD;"># 打包</span></span>
<span class="line"><span style="color:#A6ACCD;">pnpm run build</span></span>
<span class="line"><span style="color:#A6ACCD;"></span></span></code></pre></div><p>打包manage-client</p><div class="language-"><button title="Copy Code" class="copy"></button><span class="lang"></span><pre class="shiki material-palenight"><code><span class="line"><span style="color:#A6ACCD;"># 进入manage-client目录</span></span>
<span class="line"><span style="color:#A6ACCD;">cd packages/manage-client</span></span>
<span class="line"><span style="color:#A6ACCD;"># 打包</span></span>
<span class="line"><span style="color:#A6ACCD;">pnpm run build</span></span>
<span class="line"><span style="color:#A6ACCD;"></span></span></code></pre></div><h3 id="部署" tabindex="-1">部署 <a class="header-anchor" href="#部署" aria-hidden="true">#</a></h3><p>项目使用可以使用nginx部署，需要先将打包好的文件复制到nginx的html目录下，然后配置nginx nginx配置文件如下：</p><div class="language-"><button title="Copy Code" class="copy"></button><span class="lang"></span><pre class="shiki material-palenight"><code><span class="line"><span style="color:#A6ACCD;">server {</span></span>
<span class="line"><span style="color:#A6ACCD;">    listen       80;</span></span>
<span class="line"><span style="color:#A6ACCD;">	server_name  localhost; #有域名可以把localhost替换为域名</span></span>
<span class="line"><span style="color:#A6ACCD;">	client_max_body_size 1024M;</span></span>
<span class="line"><span style="color:#A6ACCD;"></span></span>
<span class="line"><span style="color:#A6ACCD;">    location / {</span></span>
<span class="line"><span style="color:#A6ACCD;">        root /usr/share/nginx/html/web;</span></span>
<span class="line"><span style="color:#A6ACCD;">        index index.html index.htm;</span></span>
<span class="line"><span style="color:#A6ACCD;">        try_files $uri $uri/ @web;</span></span>
<span class="line"><span style="color:#A6ACCD;">    }</span></span>
<span class="line"><span style="color:#A6ACCD;"></span></span>
<span class="line"><span style="color:#A6ACCD;">    # 解决history路由问题</span></span>
<span class="line"><span style="color:#A6ACCD;">    location @web {</span></span>
<span class="line"><span style="color:#A6ACCD;">        rewrite ^.*$ /index.html;</span></span>
<span class="line"><span style="color:#A6ACCD;">    }</span></span>
<span class="line"><span style="color:#A6ACCD;"></span></span>
<span class="line"><span style="color:#A6ACCD;">    #后台管理</span></span>
<span class="line"><span style="color:#A6ACCD;">    location /manage/ {</span></span>
<span class="line"><span style="color:#A6ACCD;">        root /usr/share/nginx/html;</span></span>
<span class="line"><span style="color:#A6ACCD;">        index index.html index.htm;</span></span>
<span class="line"><span style="color:#A6ACCD;">        try_files $uri $uri/ @manage;</span></span>
<span class="line"><span style="color:#A6ACCD;">    }</span></span>
<span class="line"><span style="color:#A6ACCD;"></span></span>
<span class="line"><span style="color:#A6ACCD;">    # 解决后台管理history路由问题</span></span>
<span class="line"><span style="color:#A6ACCD;">    location @manage {</span></span>
<span class="line"><span style="color:#A6ACCD;">        rewrite ^.*$ /manage/index.html;</span></span>
<span class="line"><span style="color:#A6ACCD;">    }</span></span>
<span class="line"><span style="color:#A6ACCD;"></span></span>
<span class="line"><span style="color:#A6ACCD;">}</span></span>
<span class="line"><span style="color:#A6ACCD;"></span></span></code></pre></div>`,17),c=[p];function o(t,i,r,C,A,d){return n(),a("div",null,c)}const D=s(e,[["render",o]]);export{h as __pageData,D as default};
