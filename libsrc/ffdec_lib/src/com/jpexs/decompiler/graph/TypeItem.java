/*
 *  Copyright (C) 2010-2023 JPEXS, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.jpexs.decompiler.graph;

import com.jpexs.decompiler.flash.IdentifiersDeobfuscation;
import com.jpexs.decompiler.flash.SourceGeneratorLocalData;
import com.jpexs.decompiler.flash.helpers.GraphTextWriter;
import com.jpexs.decompiler.flash.helpers.hilight.HighlightSpecialType;
import com.jpexs.decompiler.graph.model.LocalData;
import com.jpexs.decompiler.graph.model.UnboundedTypeItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author JPEXS
 */
public class TypeItem extends GraphTargetItem {

    public static TypeItem BOOLEAN = new TypeItem(DottedChain.BOOLEAN);

    public static TypeItem STRING = new TypeItem(DottedChain.STRING);
    
    public static TypeItem NUMBER = new TypeItem(DottedChain.NUMBER);
    
    public static TypeItem INT = new TypeItem(DottedChain.INT);
    
    public static TypeItem UINT = new TypeItem(DottedChain.UINT);
    
    public static TypeItem UNDEFINED = new TypeItem(DottedChain.UNDEFINED);

    public static TypeItem ARRAY = new TypeItem(DottedChain.ARRAY);

    public static UnboundedTypeItem UNBOUNDED = new UnboundedTypeItem();
    
    public static TypeItem UNKNOWN = new TypeItem("--UNKNOWN--");

    public final DottedChain fullTypeName;
    
    public boolean printRaw = false;
    
    public String ns;

    public TypeItem(String s) {
        this(s, null);
    }
    public TypeItem(String s, String ns) {
        this(s == null ? new DottedChain(new String[]{}, new String[]{""}) : DottedChain.parseWithSuffix(s), ns);
    }

    public TypeItem(DottedChain fullTypeName) {
        this(fullTypeName, (String)null);
    }            
    public TypeItem(DottedChain fullTypeName, String ns) {
        this(fullTypeName, new ArrayList<>(), ns);
   }

    public TypeItem(DottedChain fullTypeName, List<GraphTargetItem> subtypes, String ns) {
        super(null, null, NOPRECEDENCE);
        this.fullTypeName = fullTypeName;
        this.ns = ns;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.fullTypeName);
        hash = 17 * hash + Objects.hashCode(this.ns);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TypeItem other = (TypeItem) obj;
        if (!Objects.equals(this.ns, other.ns)) {
            return false;
        }
        return Objects.equals(this.fullTypeName, other.fullTypeName);
    }

    

    @Override
    public GraphTextWriter appendTo(GraphTextWriter writer, LocalData localData) throws InterruptedException {
        boolean as3 = localData.constantsAvm2 != null;

        if (localData.fullyQualifiedNames.contains(DottedChain.parseWithSuffix(fullTypeName.getLast()))) {
            writer.hilightSpecial(fullTypeName.toPrintableString(as3), HighlightSpecialType.TYPE_NAME, fullTypeName.toRawString());
        } else {
            writer.hilightSpecial(IdentifiersDeobfuscation.printIdentifier(as3, fullTypeName.getLast()), HighlightSpecialType.TYPE_NAME, fullTypeName.toRawString());
        }

        return writer;
    }

    @Override
    public GraphTargetItem returnType() {
        return this;
    }

    @Override
    public boolean hasReturnValue() {
        return true;
    }

    @Override
    public String toString() {
        return fullTypeName.toRawString();
    }

    @Override
    public List<GraphSourceItem> toSource(SourceGeneratorLocalData localData, SourceGenerator generator) throws CompilationException {
        return generator.generate(localData, this);
    }
}
