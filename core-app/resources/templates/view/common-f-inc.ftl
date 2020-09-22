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

/* =================================== 2018-01-24 add =================================== */
/* OLD css for Form/FormGroup input warning info */
/* =================================== 2018-01-24 add =================================== */
.form-control-success,
.form-control-warning,
.form-control-danger {
  padding-right: 2.25rem;
  background-repeat: no-repeat;
  background-position: center right 0.5625rem;
  -webkit-background-size: 1.125rem 1.125rem;
          background-size: 1.125rem 1.125rem;
}

.has-success .form-control-feedback,
.has-success .form-control-label,
.has-success .col-form-label,
.has-success .form-check-label,
.has-success .custom-control {
  color: #5cb85c;
}

.has-success .form-control {
  border-color: #5cb85c;
}

.has-success .input-group-addon {
  color: #5cb85c;
  border-color: #5cb85c;
  background-color: #eaf6ea;
}

.has-success .form-control-success {
  background-image: url("data:image/svg+xml;charset=utf8,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 8 8'%3E%3Cpath fill='%235cb85c' d='M2.3 6.73L.6 4.53c-.4-1.04.46-1.4 1.1-.8l1.1 1.4 3.4-3.8c.6-.63 1.6-.27 1.2.7l-4 4.6c-.43.5-.8.4-1.1.1z'/%3E%3C/svg%3E");
}

.has-warning .form-control-feedback,
.has-warning .form-control-label,
.has-warning .col-form-label,
.has-warning .form-check-label,
.has-warning .custom-control {
  color: #f0ad4e;
}

.has-warning .form-control {
  border-color: #f0ad4e;
}

.has-warning .input-group-addon {
  color: #f0ad4e;
  border-color: #f0ad4e;
  background-color: white;
}

.has-warning .form-control-warning {
  background-image: url("data:image/svg+xml;charset=utf8,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 8 8'%3E%3Cpath fill='%23f0ad4e' d='M4.4 5.324h-.8v-2.46h.8zm0 1.42h-.8V5.89h.8zM3.76.63L.04 7.075c-.115.2.016.425.26.426h7.397c.242 0 .372-.226.258-.426C6.726 4.924 5.47 2.79 4.253.63c-.113-.174-.39-.174-.494 0z'/%3E%3C/svg%3E");
}

.has-danger .form-control-feedback,
.has-danger .form-control-label,
.has-danger .col-form-label,
.has-danger .form-check-label,
.has-danger .custom-control {
  color: #d9534f;
}

.has-danger .form-control {
  border-color: #d9534f;
}

.has-danger .input-group-addon {
  color: #d9534f;
  border-color: #d9534f;
  background-color: #fdf7f7;
}

.has-danger .form-control-danger {
  background-image: url("data:image/svg+xml;charset=utf8,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='%23d9534f' viewBox='-2 -2 7 7'%3E%3Cpath stroke='%23d9534f' d='M0 0l3 3m0-3L0 3'/%3E%3Ccircle r='.5'/%3E%3Ccircle cx='3' r='.5'/%3E%3Ccircle cy='3' r='.5'/%3E%3Ccircle cx='3' cy='3' r='.5'/%3E%3C/svg%3E");
}
/* =================================== 2018-01-24 add =================================== */


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