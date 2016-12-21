var gulp = require('gulp');
var sass = require('gulp-sass');
var minifyCss = require('gulp-minify-css');
var sourceMaps = require('gulp-sourcemaps');
var rename = require('gulp-rename');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var amdOptimize = require('amd-optimize');

var designs = 'src/main/resources/jcr_root/etc/slick/designs/slick';

gulp.task('styles', function() {
    gulp.src('./**/src/scss/*.scss', {base: '.'})
        .pipe(sourceMaps.init())
        .pipe(sass().on('error', sass.logError))
        .pipe(rename(function (path) {
            path.dirname += "/../../dist/css";
            return path;
        }))
        .pipe(minifyCss())
        .pipe(sourceMaps.write('.'))
        .pipe(gulp.dest('.'))
});

gulp.task('publish-scripts', function ()
{
    return gulp.src(designs + '/src/js/**/*.js')
        .pipe(amdOptimize('publish'))
        .pipe(sourceMaps.init())
        .pipe(concat('publish.js'))
        .pipe(uglify())
        .pipe(sourceMaps.write('.'))
        .pipe(gulp.dest(designs + '/dist/js'));
});

gulp.task('author-scripts', function ()
{
    return gulp.src(designs + '/src/js/**/*.js')
        .pipe(amdOptimize('author', {
            baseUrl : designs + "/src/js",
            configFile : designs + "/src/js/config.js" }))
        .pipe(sourceMaps.init())
        .pipe(concat('author.js'))
        .pipe(uglify())
        .pipe(sourceMaps.write('.'))
        .pipe(gulp.dest(designs + '/dist/js'));
});

//Watch task
gulp.task('default',function() {
    gulp.watch('./**/scss/*.scss',['styles']);
    gulp.watch('./**/js/publish/*.js',['publish-scripts']);
});

gulp.task('build', ['styles','publish-scripts', 'author-scripts'], function() {});