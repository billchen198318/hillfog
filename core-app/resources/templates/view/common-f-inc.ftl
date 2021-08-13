<#macro commonFormHeadResource>
<script type="text/javascript" src="${qifu_basePath}js/popper.min.js?ver=${qifu_jsVerBuild}"></script>
<script type="text/javascript" src="${qifu_basePath}js/jquery-3.5.1.js?ver=${qifu_jsVerBuild}"></script>
<link rel="stylesheet" href="${qifu_basePath}css/main.css?ver=${qifu_jsVerBuild}" >
<link href="${qifu_basePath}font-awesome/css/font-awesome.min.css?ver=${qifu_jsVerBuild}" rel="stylesheet" type="text/css" />
<script src="${qifu_basePath}js/bootstrap.js?ver=${qifu_jsVerBuild}" ></script>
<script src="${qifu_basePath}configJs.js?ver=${qifu_jsVerBuild}" ></script>
<script src="${qifu_basePath}js/f.js?ver=${qifu_jsVerBuild}" ></script>

<!-- The javascript plugin to display page loading on top-->
<script src="${qifu_basePath}js/plugins/pace.min.js?ver=${qifu_jsVerBuild}"></script>

<style>

/* =================================== 2018-03-21 add =================================== */
/* width for content */
/* =================================== 2018-03-21 add =================================== */
body {
  width: 96vw; /* 2020-09-09 add, before 100% */
  margin:0 auto;
}
/* =================================== 2018-03-21 add =================================== */



/* =================================== 2018-04-19 add =================================== */
/* need with toolbar top, p style="margin-bottom: 20px" */
/* =================================== 2018-04-19 add =================================== */
.app-title {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
      -ms-flex-align: center;
          align-items: center;
  -webkit-box-pack: justify;
      -ms-flex-pack: justify;
          justify-content: space-between;
  -webkit-box-orient: horizontal;
  -webkit-box-direction: normal;
      -ms-flex-direction: row;
          flex-direction: row;
  background-color: #FFF;
  margin: -30px -30px 30px;
  padding: 10px 30px; /* 2018-04-19, 20px 30px change to 10px 30px , default please view /bootstrap-vali/css/main.css */
  -webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
          box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

@media (max-width: 480px) {
  .app-title {
    margin: -15px -15px 15px;
    padding: 10px; /* 2018-04-19, 20px change to 10px , default please view /bootstrap-vali/css/main.css */
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
        -ms-flex-direction: column;
            flex-direction: column;
    -webkit-box-align: start;
        -ms-flex-align: start;
            align-items: flex-start;
  }
}
/* =================================== 2018-04-19 add =================================== */


</style>
</#macro>