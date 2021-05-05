My Play Framework based blog and twitter analyzer and much more...!

## Useful Heroku Commands
```
% heroku stack:set cedar-14 -a bharathplays
% git push heroku master
% heroku config:set SBT_CLEAN=true
% heroku config:unset SBT_OPTS
% heroku open
% heroku logs --tail
% heroku ps
% heroku ps:scale web=1
```

## Other Stuff
react project is in modules/react/javascript -
```
cd modules/react/javascript
npm run start
```
this will start the NPM webserver on default port 3000
the `homepage` variable in `package.json` is mentioned as `/assets/react` - so the URL in the browser will be http://localhost:3000/assets/react/index.html

---

now, to generate the optimized build - 
```
npm run build
```
this will generate the optimized build and move it to `\playing\public\react` directory. the instruction to do so is in `package.json`. note that the optimized build npm creates has `static` in its name for css/js/media but `index.html` is in the `react` dir

files in the top level `/public` dir are mapped by the default `assets` route in top level Play router. so on running the play webserver, the path to access the JS is http://localhost:9000/assets/react/index.html

---

### design for the react project

1. build REST routes under `/modules/react` project
2. build the complete blog React app under `/modules/react/javascript`
3. as of now the `posts` is in `/modules/blog` - this has to be moved to a common dir wherein both `blog` and `react` modules can access. basically, host 2 parallel blog apps - one built using Play's twirl template engine and other in the more modern style
