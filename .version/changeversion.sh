old="4.0.0"
new="4.0.1"

find ../ -type f \( -name "pom.xml" \) \
  -exec sed -i -e "s/<revision>$old<\/revision>/<revision>$new<\/revision>/g" {} \;
find ../ -type f \( -name "README.md" \) \
  -exec sed -i -e "s/<version>$old<\/version>/<version>$new<\/version>/g; s/escpos-coffee:$old/escpos-coffee:$new/g" {} \;
