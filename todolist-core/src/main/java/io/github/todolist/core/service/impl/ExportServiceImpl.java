/*
 * The MIT License
 *
 * Copyright (c) 2013, benas (md.benhassine@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.todolist.core.service.impl;

import com.google.gson.Gson;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.thoughtworks.xstream.XStream;
import io.github.todolist.core.domain.Todo;
import io.github.todolist.core.service.api.ExportService;
import io.github.todolist.core.util.ExportFormat;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Implementation of {@link ExportService}.
 * @author benas (md.benhassine@gmail.com)
 */
public class ExportServiceImpl implements ExportService {

    private XStream xStream;

    private Gson gson;

    public ExportServiceImpl() {
        xStream = new XStream();
        xStream.alias("todo", Todo.class);
        gson = new Gson();
    }

    /**
     * {@inheritDoc}
     */
    public byte[] exportTodoList(final List<Todo> todoList, final ExportFormat exportFormat) {

        if (exportFormat.equals(ExportFormat.XML)) {
            return xStream.toXML(todoList).getBytes();
        }

        if (exportFormat.equals(ExportFormat.PDF)) {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                PdfWriter.getInstance(document, baos);
                document.open();
                for (Todo todo :todoList) {
                    document.add(new Paragraph(todo.toString()));
                }
            } catch (DocumentException de) {
                return null;
            }
            document.close();
            return baos.toByteArray();
        }

        if (exportFormat.equals(ExportFormat.JSON)) {
            return gson.toJson(todoList).getBytes();
        }

        return new byte[]{};
    }
}
