
<div style="text-align: center">
    <span style="font-size: 3em; font-weight: 700; font-family: Consolas">
        Lab 01 <br>
        A Gentle Introduction to Hadoop
    </span>
    <br><br>
    <span style="">
        A assignment for <code>CSC14118</code> Introduction to Big Data @ 20KHMT1
    </span>
    <br><br>
</div>

## Collaborators (your-team-name)

- `xxxxxxxx` **Member 1** ([@githubaccount1](https://github.com/githubaccount1))
- `yyyyyyyy` **Member 2** ([@githubaccount2](https://github.com/githubaccount2))
- `zzzzzzzz` **Member 3** ([@githubaccount3](https://github.com/githubaccount3))
- `uuuuuuuu` **Member 4** ([@githubaccount4](https://github.com/githubaccount4))

## Instructors

- `HCMUS` **Đoàn Đình Toàn** ([@ddtoan](ddtoan18@clc.fitus.edu.vn))
- `HCMUS` **Nguyễn Ngọc Thảo** ([@nnthao](nnthao@fit.hcmus.edu.vn))

---
<div style="page-break-after: always"></div>

## Quick run
>
> You can clear this section and insert your own instruction.

To export your report with the [OSCP](https://help.offensive-security.com/hc/en-us/articles/360046787731-PEN-200-Reporting-Requirements) template, you should install the following packages:

For Archlinux:

```bash
pacman -S texlive-most pandoc
```

For Ubuntu:

```bash
apt install texlive-latex-recommended texlive-fonts-extra texlive-latex-extra pandoc
```

Then using the `convert_md_to_pdf.sh` to export your report to pdf.

> For those who don't want to use OSCP template, you can use alternative ways to export your `report.md` to `pdf` (`Typora`, `pandoc` without `Latex`, `Obsidian`,...) but please keep the `yaml` header of the report as follow:

```yaml
---
title: "Lab 01: A Gentle Introduction to Hadoop"
author: ["your-team-name"]
date: "yyyy-mm-dd"
subtitle: "CSC14118 Introduction to Big Data 20KHMT1"
lang: "en"
titlepage: true
titlepage-color: "0B1887"
titlepage-text-color: "FFFFFF"
titlepage-rule-color: "FFFFFF"
titlepage-rule-height: 2
book: true
classoption: oneside
code-block-font-size: \scriptsize
---
```