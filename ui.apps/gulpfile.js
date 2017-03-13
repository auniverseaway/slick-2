const gulp        = require('gulp');
const sass        = require('gulp-sass');
const minifyCss   = require('gulp-minify-css');
const sourceMaps  = require('gulp-sourcemaps');
const rename      = require('gulp-rename');
const concat      = require('gulp-concat');
const uglify      = require('gulp-uglify');
const amdOptimize = require('amd-optimize');
const slang       = require('gulp-slang');
const argv        = require('yargs').argv;

const slingHost = argv.slingHost ? argv.slingHost : 'localhost';
const slingPort = argv.slingPort ? argv.slingPort : 8080;
const slingPass = argv.slingPass ? argv.slingPass : 'admin';
const slingUser = argv.slingUser ? argv.slingUser : 'admin';

const designs = 'src/main/resources/jcr_root/etc/slick/designs/slick';

gulp.task('publish-scripts', function ()
{
    return gulp.src(designs + '/src/js/**/*.js')
        .pipe(amdOptimize('publish', {
            baseUrl : designs + '/src/js',
            configFile : designs + '/src/js/config.js' }))
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

gulp.task('styles', function() {
    return gulp.src(designs + '/src/scss/**/*.scss')
        .pipe(sourceMaps.init())
        .pipe(sass().on('error', sass.logError))
        .pipe(minifyCss())
        .pipe(sourceMaps.write('.'))
        .pipe(gulp.dest(designs + '/dist/css'));
});

gulp.task('default',function() {
    gulp.watch('./**/scss/**/*.scss',['styles']);
    gulp.watch('./**/js/publish/*.js',['publish-scripts']);
    gulp.watch('./**/js/author/*.js',['author-scripts']);
    
    gulp.watch(designs + '/dist/**/*.*', function(event) {
        return gulp.src(event.path).pipe(slang({ host: slingHost, port: slingPort, username: slingUser, password: slingPass }));
    });
});

gulp.task('build', ['styles','publish-scripts', 'author-scripts'], function() {});