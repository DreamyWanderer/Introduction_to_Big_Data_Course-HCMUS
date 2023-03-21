pandoc -H templates/disable_float.tex docs/report.md \
-o docs/report.pdf \
--from markdown+yaml_metadata_block-markdown_in_html_blocks+raw_html \
--template templates/eisvogel.latex \
--table-of-contents \
--toc-depth 6 \
--number-sections \
--top-level-division=chapter \
--highlight-style breezedark \
--shift-heading-level-by=-1 \
--resource-path=.:docs
