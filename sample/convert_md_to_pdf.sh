pandoc docs/report.md \
-o docs/report.pdf \
--from markdown+yaml_metadata_block+raw_html \
--template templates/eisvogel.latex \
--table-of-contents \
--toc-depth 6 \
--number-sections \
--top-level-division=chapter \
--highlight-style breezedark \
--resource-path=.:docs
