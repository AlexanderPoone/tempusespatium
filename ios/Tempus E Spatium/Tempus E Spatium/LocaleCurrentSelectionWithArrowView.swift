//
//  LocaleCurrentSelectionWithArrowView.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 18/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit

class LocaleCurrentSelectionWithArrowView: UIView {

    let nibName = "LocaleCurrentSelectionWithArrowView"    
    @IBOutlet var mContentView: UIView!
    @IBOutlet weak var mFlag: UIImageView!
    @IBOutlet weak var mSelection: UILabel!
    @IBOutlet weak var mSubtitle: UILabel!
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        Bundle.main.loadNibNamed(nibName, owner: self, options: nil)
        addSubview(mContentView)
        mContentView.frame = self.bounds
        mContentView.autoresizingMask = [.flexibleHeight, .flexibleWidth]
    }

}
